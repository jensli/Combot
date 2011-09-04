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
import j.combot.gui.visuals.CommandVisual;
import j.combot.gui.visuals.GuiArgVisual;
import j.combot.gui.visuals.IntVisual;
import j.combot.gui.visuals.NullVisual;
import j.combot.gui.visuals.OptVisual;
import j.combot.gui.visuals.StringVisual;
import j.combot.gui.visuals.VisualFactory;
import j.combot.gui.visuals.VisualTypes;
import j.swt.util.SwtStdValues;
import j.swt.util.SwtUtil;
import j.util.functional.Action0;
import j.util.functional.Fun1;
import j.util.util.Asserts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.prefs.Preferences;

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
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
	private StackLayout cmdStackLayout;

	private InputBox defaultsNameBox;

	private StyledText outputText;

	private ToolItem delItem;

	private CommandData commands = new CommandData(
			new Fun1<CommandData, Command>() {
				public Command run( CommandData arg ) {
					return arg.cmd;
				}
			} );

	private final VisualFactory visualFactory;
	private final VisFact<?>[] VIS_FACTS;

	private Map<Arg<?>, List<ValEntry>> errorMap = new HashMap<>();

	// Is set as validation listener for all the ArgVisuals, is run every time
	// something changes in the arg input controls.
	// Sets enabled status for start button and tooltip.
	private ValidationListener valLis;

	private CombotApp app;
	private CommandData activeCommand;


	public CombotGui( CombotApp app ) {
		visualFactory = new VisualFactory();
		this.app = app;
	}

	public void init()
	{
		Display.setAppName( Globals.APP_NAME );
		display = new Display();
		SwtStdValues.init( display );

//		cmdData.setData( null ); // The dummy root command

		visualFactory.addValidationListener( valLis );
		visualFactory.addAll( VIS_FACTS );
		shell = makeShell( display );

		makeGui( shell );
	}


	public Shell makeShell( Display display )
	{
		Shell shell = new Shell( display );
		shell.setLayout( new FillLayout() );
		shell.setText( Globals.APP_NAME );

		shell.addShellListener( new ShellAdapter() {
			@Override public void shellClosed( ShellEvent e ) {
				onQuit();
			}
		} );

		return shell;
	}



	private void onQuit() {
		app.onQuit( commands );
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

	public Command getActiveCmd() {
		return getActiveCmdData().cmd;
	}

	private CommandData getActiveCmdData() {
		return activeCommand;
	}

	public void setCommadList( CommandContainer commands )
	{
		for ( Entry<Command, ArgGroup> parent : commands ) {
			addCommand( parent.getKey() );
			for ( Arg<?> a : parent.getValue() ) {
				addChildCommand( parent.getKey(), (Command) a );
			}
		}
	}

	private void initCmd( TreeItem item, Command cmd, CommandData parent )
	{
		item.setText( cmd.getTitle() );
		Composite panel = makeCommandPanel( cmd, commandPanel, visualFactory );
		CommandData cData = new CommandData( cmd, panel, item );
		parent.addChild( cData  );
		item.setData( cData );
	}

	public void addChildCommand( Command parent, Command child )
	{
		CommandData parentData = commands.getChild( parent );
		Asserts.notNull( parentData, "Parent command do not exist in gui" );

		TreeItem childItem = new TreeItem( parentData.item, NONE );

		initCmd( childItem, child, parentData  );

		switchActiveCommand( parentData.getChild( child ) );
	}

	public void addCommand( Command cmd )
	{
		TreeItem item = new TreeItem( tree, NONE );
		initCmd( item, cmd, commands );
	}


	// Switches to a new command panel, creating all the widgets.
	public void switchActiveCommand( CommandData cmd )
	{
		delItem.setEnabled( cmd.getParent().hasParent() );
		tree.setSelection( cmd.item );

		activeCommand = cmd;

		cmdStackLayout.topControl =  cmd.page;
		commandPanel.layout();
	}

	private Composite makeGui( Composite parent )
	{
		Composite gui = new Composite( parent, NONE );
		gui.setLayout( new GridLayout( 2, false ) );

		Composite listPanel = makeListPanel( gui );
		listPanel.setLayoutData( new GridData( LEFT, FILL, false, true) );


		rightPanel = new Composite( gui, NONE );
		rightPanel.setLayout( new GridLayout( 1, false ) );
		rightPanel.setLayoutData( new GridData( FILL, FILL, true, true ) );

		commandPanel = new Composite( rightPanel, NONE );
		commandPanel.setLayoutData( new GridData( FILL, FILL, true, true ) );
		cmdStackLayout = new StackLayout();
		commandPanel.setLayout( cmdStackLayout );

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


		delItem = new ToolItem( toolBar, PUSH );
		delItem.setText( "Delete" );
		delItem.addSelectionListener( new SelectionAdapter() {
			@Override public void widgetSelected( SelectionEvent e ) {
				delCurrentCmd();
			}
		} );

		tree.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				switchActiveCommand( (CommandData) e.item.getData() );
			}
		} );

		defaultsNameBox = new InputBox( shell );
		defaultsNameBox.setText( "New defaults" );
		defaultsNameBox.setMessage( "Name for saved defaults:" );
		defaultsNameBox.setTrim( true );

		defaultsNameBox.setResultCallback( new Action0() {
			public void run() { saveCurrentDefaults(); }
		} );


		panel.pack();

		return panel;
	}

	private void delCurrentCmd()
	{
		CommandData cmdToDel = getActiveCmdData();

		switchActiveCommand( cmdToDel.getParent() );

		cmdToDel.item.dispose();

		cmdToDel.removeSelf();


//		app.deleteCmd( cmdToDel.cmd );
	}

	private void saveCurrentDefaults()
	{
		if ( !defaultsNameBox.isOk() ) return;

		String newTitle = defaultsNameBox.getResult();

		CommandData data = getActiveCmdData();
		CommandData parentData = data.getParent().hasParent() ?
				data.getParent() : data;

		app.makeNewCommand( parentData.cmd, data.cmd, newTitle );
	}


	@SuppressWarnings( { "rawtypes", "unchecked" } )
	private static Composite makeCommandPanel( Command cmd, Composite parent, VisualFactory visualFactory )
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
			}
		});

		// Stop button
		stopButton = new Button( controls, PUSH );
		stopButton.setText( "Stop" );
		stopButton.setEnabled( false );

		stopButton.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				app.stopCommand();
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

	private void startCreateDefaults()
	{

		List<String> list = new ArrayList<>();
		for ( CommandData cda : Iterables.skip( commands, 1 ) ) {
			list.add( cda.cmd.getTitle() );
		}

		defaultsNameBox.setValidator(
				new CombinedValidator<>(
						Validator.EMPTY_VALIDATOR,
						new EnumValidator( list, true ) ) );

		defaultsNameBox.open();
	}

	public void onExit( Preferences prefs ) {

	}

	// Init
	{
		valLis = new ValidationListener() {
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
			} };


			VIS_FACTS = new VisFact[] {
					new VisFact<>( VisualTypes.INT_TYPE, IntVisual.class ),
					new VisFact<>( VisualTypes.STRING_TYPE, StringVisual.class ),
					new VisFact<>( VisualTypes.COMMAND_TYPE, CommandVisual.class ),
					new VisFact<>( VisualTypes.NULL_TYPE, NullVisual.class ),
					new VisFact<>( VisualTypes.OPT_TYPE, OptVisual.class ),
			};
	}

}

