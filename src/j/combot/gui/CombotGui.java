package j.combot.gui;

import static org.eclipse.swt.SWT.BEGINNING;
import static org.eclipse.swt.SWT.BORDER;
import static org.eclipse.swt.SWT.DEFAULT;
import static org.eclipse.swt.SWT.FILL;
import static org.eclipse.swt.SWT.HORIZONTAL;
import static org.eclipse.swt.SWT.H_SCROLL;
import static org.eclipse.swt.SWT.LEFT;
import static org.eclipse.swt.SWT.NONE;
import static org.eclipse.swt.SWT.PUSH;
import static org.eclipse.swt.SWT.SEPARATOR;
import static org.eclipse.swt.SWT.SINGLE;
import static org.eclipse.swt.SWT.TOP;
import static org.eclipse.swt.SWT.V_SCROLL;
import j.combot.Globals;
import j.combot.app.CombotApp;
import j.combot.app.CommandContainer;
import j.combot.command.Arg;
import j.combot.command.ArgGroup;
import j.combot.command.CombinedValidator;
import j.combot.command.Command;
import j.combot.command.EnumValidator;
import j.combot.command.ValEntry;
import j.combot.command.Validator;
import j.combot.gui.misc.GuiUtil;
import j.combot.gui.misc.InputBox;
import j.combot.gui.misc.ValidationEvent;
import j.combot.gui.misc.ValidationListener;
import j.combot.gui.visuals.GuiArgVisual;
import j.combot.gui.visuals.VisualFactory;
import j.swt.util.SwtStdValues;
import j.swt.util.SwtUtil;
import j.util.functional.Action0;
import j.util.tree.TreeImpl;
import j.util.util.Asserts;
import j.util.util.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.google.common.collect.Iterables;



public class CombotGui
{
	// Controls
	private Display display;
	private Shell shell;

	private Label status;
	private Label command;

	private Button stopButton;
	private Button startButton;

	private Composite controls;

	private Tree tree;
	private Composite rightPanel;

	private Composite commandPanel;
	private InputBox defaultsNameBox;


	private StyledText outputText;

	private final VisualFactory visualFactory;

//	private BasicCaller0
//		startCaller = new BasicCaller0(),
//		stopCaller = new BasicCaller0();

//	private BasicCaller<NewCommandEvent> defaultCommandCaller = new BasicCaller<>();

	@SuppressWarnings( "unused" )
//	private List<Command> commands;

	private CommandContainer commands;

	@SuppressWarnings( "unused" )
	private boolean isCommandRunning = false;

	private Map<Arg<?>, List<ValEntry>> errorMap = new HashMap<>();

	// Is set as validation listener for all the ArgVisuals, is run every time
	// something changes in the arg input controls.
	// Sets enabled status for start button and tooltip.
	private ValidationListener valLis = new ValidationListener() {
		public void visualValidated( ValidationEvent e )
		{
			if ( e.entries.isEmpty() ) {
				errorMap.remove( e.sender );
			} else {
				errorMap.put( e.sender, e.entries );
			}

			startButton.setEnabled( errorMap.isEmpty() );

			String toolTip = errorMap.isEmpty() ?  "" : "Can not start because of errors";
			for ( ValEntry en : Iterables.concat( errorMap.values() ) ) {
				toolTip = toolTip + "\n" + en.sender.getTitle() + ": " + en.message;
			}

			controls.setToolTipText( toolTip );
		}
	};

	private CombotApp app;
	private StackLayout cmdStackLayout;


	public CombotGui( CombotApp app ) {
		visualFactory = new VisualFactory();
		this.app = app;
	}

	public Command getActiveCmd() {
		return ((CommandData) tree.getSelection()[ 0 ].getData()).cmd;
	}

	public CommandData getActiveCmdData() {
		return (CommandData) tree.getSelection()[ 0 ].getData();
	}

	public void init()
	{
		Display.setAppName( Globals.APP_NAME );
		display = new Display();
		SwtStdValues.init( display );

		visualFactory.addValidationListener( valLis );

		visualFactory.addAll( GuiGlobals.VIS_FACTS );
		shell = makeShell( display );

		makeGui( shell );
	}


	public static Shell makeShell( Display display )
	{
		Shell shell = new Shell( display );
		shell.setLayout( new FillLayout() );
		shell.setText( Globals.APP_NAME );
		return shell;
	}

	public void receiveOutput( final String line )
	{
		display.syncExec( new Runnable() {
			public void run() {
				outputText.append( line + "\n" );
//				SwtUtil.scrollToMax( outputText.getHorizontalBar() );
			}
		});
	}

	public void receiveError( final String line )
	{
		display.syncExec( new Runnable() {
			public void run() {
				SwtUtil.appendStyled( outputText, line + "\n", SwtStdValues.RED );
//				SwtUtil.scrollToMax( outputText.getHorizontalBar() );
			}
		});
	}

	public void signalTerminated( final int code ) {
		display.syncExec( new Runnable() {
			public void run() { onHasTerminated( code ); }
		} );
	}

	public void onHasTerminated( int code )
	{
		setStatus( "Terminated with exit code " + code );
		setCommandRunning( false );
	}

	public void setCommadList( CommandContainer commands )
	{
//		this.commands = new ArgGroup( commands );

		for ( Entry<Command, ArgGroup> parent : commands ) {
			addCommand( parent.getKey() );
			for ( Arg<?> a : parent.getValue() ) {
				addChildCommand( parent.getKey(), (Command) a );
			}
		}
	}

	private void initCmd( TreeItem item, Command cmd, j.util.tree.Tree<CommandData> parent )
	{
		item.setText( cmd.getTitle() );
		Composite panel = makeCommandPanel( cmd, commandPanel );
		CommandData cData = new CommandData( cmd, panel, item );
		parent.add( cData  );
		item.setData( cData );
	}

	public void addChildCommand( Command parent, Command child )
	{
		j.util.tree.Tree<CommandData> p = cmdData.findSubtree( parent );
		CommandData parentData = p.getRoot();
		Asserts.notNull( parentData, "Parent command do not exist in gui" );

		TreeItem childItem = new TreeItem( parentData.item, NONE );

		initCmd( childItem, child, p  );
		tree.setSelection( childItem );

//		childItem.setText( child.getTitle() );
//		Composite panel = makeCommandPanel( child, commandPanel );
//		CommandData cData = new CommandData( child, panel, childItem );
//		cmdData.add( cData  );
//		childItem.setData( cData );




//		for ( TreeItem item : tree.getItems() ) {
//			if ( item.getData().equals( parent ) ) {
//				TreeItem c = new TreeItem( item, NONE );
//				c.setText( child.getTitle() );
//				c.setData( child );
//				tree.setSelection( c );
//
//				Composite panel = makeCommandPanel( child, commandPanel );
//				cmdData.add( new CommandData( child, panel, c )  );
//
//				return;
//			}
//		}

	}

//	private void initTreeItem( Item

	public void addCommand( Command cmd )
	{
		TreeItem item = new TreeItem( tree, NONE );
		initCmd( item, cmd, cmdData );
	}


	// Switches to a new command panel, creating all the widgets.
	public void switchActiveCommand( CommandData cmd )
	{
//		if ( commandPanel != null ) {
//			commandPanel.dispose();
//		}
//
//		commandPanel = makeCommandPanel( cmd, rightPanel );
//		commandPanel.setLayoutData( new GridData( FILL, TOP, true, false ) );
//		commandPanel.moveAbove( rightPanel.getChildren()[ 0 ] );

		cmdStackLayout.topControl =  cmd.page;
		commandPanel.layout();
	}

	j.util.tree.Tree<CommandData> cmdData = new TreeImpl<>( new CmdCmp() );

	static class CommandData {

		public CommandData( Command cmd, Composite page, TreeItem item ) {
			this.cmd = cmd;
			this.page = page;
			this.item = item;
		}

		public Command cmd;
		public Composite page;
		public TreeItem item;

		public static List<Command> getCmds( Collection<CommandData> l ) {
			List<Command> rl = new ArrayList<>();
			for ( CommandData cd : l ) {
				rl.add( cd.cmd );
			}
			return rl;
		}
	}


	private Composite makeGui( Composite parent )
	{
		cmdData.setRoot( null );

		Composite gui = new Composite( parent, NONE );
		gui.setLayout( new GridLayout( 2, false ) );
//		gui.setLayout( new RowLayout() );

		Composite listPanel = makeListPanel( gui );
		listPanel.setLayoutData( new GridData( LEFT, FILL, false, true) );


		rightPanel = new Composite( gui, NONE );
		rightPanel.setLayout( new GridLayout( 1, false ) );
		rightPanel.setLayoutData( new GridData( FILL, FILL, true, true ) );

		commandPanel = new Composite( rightPanel, NONE );
		commandPanel.setLayoutData( new GridData( FILL, FILL, true, true ) );
		cmdStackLayout = new StackLayout();
		commandPanel.setLayout( cmdStackLayout );

//		for ( Arg<?> cmd : commands.getLinear() ) {
//			makeCommandPanel( (Command) cmd, commandPanel );
//		}

		Label argButtonSep = new Label( rightPanel, SEPARATOR | HORIZONTAL );
		argButtonSep.setLayoutData( new GridData( FILL, TOP, true, false ) );

		Composite controlPanel = makeControlPanel( rightPanel );
		controlPanel.setLayoutData( new GridData( FILL, FILL, true, true ) );

		gui.layout();

		return rightPanel;
	}

	private Composite makeListPanel( Composite parent )
	{
		Composite panel = new Composite( parent, NONE );
		panel.setLayout( new GridLayout( 1, false ) );

		Label title = new Label( panel, NONE );
//		title.setFont( GuiGlobals.TITLE_FONT );
		title.setText( "Availible commands:" );

		tree = new Tree( panel, SINGLE | BORDER );
		tree.setLayoutData( new GridData( FILL, FILL, true, true ) );

		ToolBar toolBar = new ToolBar( panel, NONE );

		ToolItem saveItem = new ToolItem( toolBar, PUSH );
		saveItem.setText( "Save as new" );
		saveItem.addSelectionListener( new SelectionAdapter() {
			@Override public void widgetSelected( SelectionEvent e ) {
				startCreateDefaults();
			}
		} );


		final ToolItem delItem = new ToolItem( toolBar, PUSH );
		delItem.setText( "Delete" );
		delItem.addSelectionListener( new SelectionAdapter() {
			@Override public void widgetSelected( SelectionEvent e ) {
				delCurrentCmd();
			}
		} );

		tree.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				TreeItem i = (TreeItem) e.item;
				delItem.setEnabled( i.getParentItem() != null );
				switchActiveCommand( (CommandData) i.getData() );
			}
		} );

		defaultsNameBox = new InputBox( shell );
		defaultsNameBox.setText( "New defaults" );
		defaultsNameBox.setMessage( "Name for saved defaults:" );
		defaultsNameBox.setTrim( true );

		defaultsNameBox.setResultCallback( new Action0() {
			public void run() { saveCurrentDefaults(); }
		} );


//		org.eclipse.swt.widgets.List listBox = new org.eclipse.swt.widgets.List( panel, SINGLE | BORDER );
//		listBox.setLayoutData( new GridData( FILL, FILL, true, true ) );
//		listBox.add( "Qwer" );
//		listBox.add( "Some other comamnd" );
		panel.pack();

		return panel;
	}

	private void delCurrentCmd()
	{
		Command cmdToDel = getActiveCmd();
		switchActiveCommand( cmdData.findSubtree( cmdToDel ).getParent().getRoot() );
		tree.getSelection()[ 0 ].dispose();
		app.deleteCmd( cmdToDel );
	}

	private void saveCurrentDefaults()
	{
		if ( !defaultsNameBox.isOk() ) return;

		String newTitle = defaultsNameBox.getResult();
		app.makeNewCommand( getActiveCmdParent(), getActiveCmd(), newTitle );
//		getNewCommandCaller().call(
//				new NewCommandEvent( getActiveCmdParent(), getActiveCmd(), newTitle ) );
	}

	/**
	 * Gets the currently parent of the currently selected command. The parent
	 * of top level command is considered to be it self.
	 */
	private Command getActiveCmdParent()
	{
		TreeItem i = tree.getSelection()[ 0 ],
				parent = i.getParentItem();
		// If there is a parent choose that, else i itself
		i = parent == null ? i : parent;

		return ((CommandData) i.getData()).cmd;
	}

	public class NewCommandEvent {
		public Command parentComamnd;
		public Command baseCmd;
		public String newTitle;



		public NewCommandEvent( Command parentComamnd, Command baseCmd,
				String newTitle ) {
			this.parentComamnd = parentComamnd;
			this.baseCmd = baseCmd;
			this.newTitle = newTitle;
		}

	}

	@SuppressWarnings( { "rawtypes", "unchecked" } )
	private Composite makeCommandPanel( Command cmd, Composite parent )
	{
		Composite panel = new Composite( parent, NONE );
		panel.setLayout( new GridLayout( 2, false ) );

		// Commmand title
		GuiArgVisual<?> commandVisual = visualFactory.make( cmd );
		commandVisual.makeWidget( (Arg) cmd, panel, visualFactory );

		Label comArgSep = new Label( panel, SEPARATOR | HORIZONTAL );
		comArgSep.setLayoutData( new GridData( FILL, TOP, true, false, 2, 1 ) );

		// Scrolling
		ScrolledComposite scrolled = new ScrolledComposite( panel, V_SCROLL );
		scrolled.setExpandHorizontal( true );
		scrolled.setShowFocusedControl( true );
		scrolled.setAlwaysShowScrollBars( true );
		scrolled.setLayoutData( new GridData( FILL, FILL, true, true) );

		// Args
		Composite argsComp = new Composite( scrolled, NONE );
		scrolled.setContent( argsComp );

		GridLayout argsLayout = new GridLayout( 2, false );
		argsComp.setLayout( argsLayout );

		// Add the acctual args
		for ( Arg<?> arg : cmd.getArgGroup() ) {
			GuiUtil.createVisual( arg, argsComp, visualFactory );
		}

		argsComp.pack();
		scrolled.pack();
		panel.pack();

		return panel;
	}

	public Composite makeControlPanel( Composite parent )
	{
		Composite panel = new Composite( parent, NONE );
		panel.setLayout( new GridLayout( 1, false ) );

		controls = new Composite( panel, NONE );
		RowLayout buttonLayout = new RowLayout( HORIZONTAL );
		buttonLayout.pack = false;
		buttonLayout.spacing = SwtStdValues.SPACING;
		controls.setLayout( buttonLayout );

		// Start button
		startButton = new Button( controls, PUSH );
		startButton.setText( "Start" );
		startButton.setLayoutData( new RowData( SwtStdValues.BUTTON_WIDTH, DEFAULT ) );

		startButton.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				app.startCmd();
//				startCaller.call();
			}
		});

		// Stop button
		stopButton = new Button( controls, PUSH );
		stopButton.setText( "Stop" );
		stopButton.setEnabled( false );

		stopButton.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				app.stopCommand();
//				stopCaller.call();
			}
		});

		// Status labels
		command = new Label( panel, NONE );
		command.setLayoutData( new GridData( FILL, BEGINNING, true, false ) );
		setCommandLine( "" );

		status = new Label( panel, NONE );
		status.setLayoutData( new GridData( FILL, BEGINNING, true, false ) );
		setStatus( "Not started" );

		// Output text
		outputText = new StyledText( panel, BORDER | V_SCROLL | H_SCROLL );
		GridData outputData = new GridData( FILL, FILL, true, true);
		outputData.minimumHeight = 100;
		outputText.setLayoutData( outputData );

		outputText.setEditable( false );

		return panel;
	}


	/**
	 *  Doesnt return until SWT is stopped
	 */
	public void run()
	{
		shell.open();

		while ( !shell.isDisposed() ) {
			if ( !display.readAndDispatch() ) {
				display.sleep();
			}
		}

		dispose();
	}


	private void setStatus( String s ) {
		status.setText( "Status: " + s );
	}

	public void onCommandStarted( String line )
	{
		setStatus( "Running" );
		setCommandLine( line );
		setCommandRunning( true );
		outputText.setText( "" );
	}


	private void setCommandLine( String line ) {
		command.setText( "Command line: " + line );
	}

	public void setCommandRunning( boolean b )
	{
		isCommandRunning = b;
		stopButton.setEnabled( b );
		startButton.setEnabled( !b );
	}

	public void onCommandStopped()
	{
		stopButton.setEnabled( false );
	}

	public void dispose() {
		display.dispose();
	}

//	public Caller<Action0> getStartCaller() {
//		return startCaller;
//	}
//
//	public Caller<Action0> getStopCaller() {
//		return stopCaller;
//	}
//
//	public BasicCaller<NewCommandEvent> getNewCommandCaller() {
//		return defaultCommandCaller;
//	}

	private void startCreateDefaults()
	{
//		Command c = getActiveCmd();
//
//		j.util.tree.Tree<CommandData> cmdTree = cmdData.findSubtree( c );
//
//		TreeItem item = tree.getSelection()[ 0 ],
//				parent = item.getParentItem();
//
//		if ( parent == null ) parent = item;

		List<String> list = new ArrayList<>();
		for ( CommandData cda : Iterables.skip( cmdData, 1 ) ) {
			list.add( cda.cmd.getTitle() );
		}

		defaultsNameBox.setValidator(
				new CombinedValidator<>(
						Validator.EMPTY_VALIDATOR,
						new EnumValidator( list, true ) ) );

		defaultsNameBox.open();
	}

	private class CmdCmp implements Comparator<Object>{
		@Override
		public int compare( Object o1, Object o2 )
		{
			if ( o1 instanceof CommandData ) o1 = ((CommandData) o1).cmd;
			if ( o2 instanceof CommandData ) o2 = ((CommandData) o2).cmd;

			return Util.equals( o1, o2 ) ? 0 : 1;
		}

	}


}

