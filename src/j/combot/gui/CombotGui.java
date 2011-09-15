package j.combot.gui;

import static org.eclipse.swt.SWT.BORDER;
import static org.eclipse.swt.SWT.FILL;
import static org.eclipse.swt.SWT.LEFT;
import static org.eclipse.swt.SWT.NONE;
import static org.eclipse.swt.SWT.PUSH;
import static org.eclipse.swt.SWT.SINGLE;
import j.combot.Globals;
import j.combot.app.CombotApp;
import j.combot.app.CommandContainer;
import j.combot.command.Arg;
import j.combot.command.ArgGroup;
import j.combot.command.Command;
import j.combot.gui.misc.InputBox;
import j.combot.gui.misc.ValidationEvent;
import j.combot.gui.misc.ValidationListener;
import j.combot.gui.visuals.AltVisual;
import j.combot.gui.visuals.CommandVisual;
import j.combot.gui.visuals.CompositeVisual;
import j.combot.gui.visuals.ConstVisual;
import j.combot.gui.visuals.IntVisual;
import j.combot.gui.visuals.NullVisual;
import j.combot.gui.visuals.OptVisual;
import j.combot.gui.visuals.StringVisual;
import j.combot.gui.visuals.VisualFactory;
import j.combot.gui.visuals.VisualTypes;
import j.combot.validator.CombinedValidator;
import j.combot.validator.EnumValidator;
import j.combot.validator.ValEntry;
import j.combot.validator.Validator;
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

import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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


	private Tree tree;
	private Composite rightPanel;

	private StackLayout cmdStackLayout;
	private Composite commandComp;
	private InputBox defaultsNameBox;

	// Delete command button
	private ToolItem delItem;

	private Map<Command, CommandPanel> commandPanelMap = new HashMap<>();

	private CommandData commands = new CommandData(
			new Fun1<CommandData, Command>() {
				public Command run( CommandData arg ) {
					return arg.cmd;
				}
			} );

	private final VisualFactory visualFactory;
	private final VisFact<?>[] VIS_FACTS;

	// Is set as validation listener for all the ArgVisuals, is run every time
	// something changes in the arg input controls.
	// Sets enabled status for start button and tooltip.
	private ValidationListener valLis;

	private CombotApp app;
	private CommandPanel activeCommand;

	private CommandPanel getCommandPanel( Command c ) {
		return commandPanelMap.get( c );
	}


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

				getActiveCommandPanel().outputText.append( line + "\n" );
//				SwtUtil.scrollToMax( outputText.getHorizontalBar() );
			}
		});
	}

	public void receiveError( final String line )
	{
		display.syncExec( new Runnable() {
			public void run() {
				SwtUtil.appendStyled( getActiveCommandPanel().outputText, line + "\n", SwtStdValues.COLOR_RED );
//				SwtUtil.scrollToMax( outputText.getHorizontalBar() );
			}
		});
	}

	public void signalTerminated( final int code ) {
		display.syncExec( new Runnable() {
			public void run() {
				onHasTerminated( code );
			}
		} );
	}

	public void onHasTerminated( int code )
	{
 		getActiveCommandPanel().setStatus( "Terminated with exit code " + code );
 		getActiveCommandPanel().setCommandRunning( false );
	}

	public Command getActiveCmd() {
		return getActiveCommandPanel().commandData.cmd;
	}


	private CommandPanel getActiveCommandPanel() {
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
		CommandData cData = new CommandData( cmd );
		parent.addChild( cData  );

		CommandPanel commandPanel = new CommandPanel( app );
		commandPanel.commandData = cData;
		commandPanel.item = item;

		item.setText( cmd.getTitle() );
		item.setData( commandPanel );
		commandPanelMap.put( cmd, commandPanel );


		Composite panel = commandPanel.makeCommandPanel( cmd, commandComp, visualFactory );
		commandPanel.comp = panel;
		switchActiveCommand( getCommandPanel( cmd ) );

//		setInitValidateResult( cmd );

		for ( ValEntry e : cmd.validate() ) {
			getActiveCommandPanel().addValidateResult( e );
		}

	}



	public void addChildCommand( Command parent, Command child )
	{
		CommandPanel parentPanel = getCommandPanel( parent );
		CommandData parentData = parentPanel.commandData;

		Asserts.notNull( parentPanel, "Parent command do not exist in gui" );

		TreeItem childItem = new TreeItem( parentPanel.item, NONE );

		initCmd( childItem, child, parentData  );

	}

	public void addCommand( Command cmd )
	{
		TreeItem item = new TreeItem( tree, NONE );
		initCmd( item, cmd, commands );
	}


	// Switches to a new command panel, creating all the widgets.
	public void switchActiveCommand( CommandPanel cmd )
	{
		delItem.setEnabled( cmd.commandData.getParent().hasParent() );
		tree.setSelection( cmd.item );

		activeCommand = cmd;

		cmdStackLayout.topControl =  cmd.comp;
		commandComp.layout();
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

		commandComp = new Composite( rightPanel, NONE );
		commandComp.setLayoutData( new GridData( FILL, FILL, true, true ) );
		cmdStackLayout = new StackLayout();
		commandComp.setLayout( cmdStackLayout );


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
				switchActiveCommand( (CommandPanel) e.item.getData() );
			}
		} );

		defaultsNameBox = new InputBox( shell );
		defaultsNameBox.setText( "New defaults" );
		defaultsNameBox.setMessage( "Name for saved defaults:" );
		defaultsNameBox.setTrim( true );

		defaultsNameBox.setResultCallback( new Action0() {
			public void run() {
				saveCurrentDefaults();
			}
		} );


		panel.pack();

		return panel;
	}

//	private void delCurrentCmd()
//	{
//		CommandPanel p = getActiveCommandPanel();
//		CommandData cmdToDel = p.commandData;
//
//		switchActiveCommand( getCommandPanel( cmdToDel.getParent().cmd ) );
//
//		p.item.dispose();
//		p.comp.dispose();
//
//		commandPanelMap.remove( p.command );
//
//		cmdToDel.removeSelf();
//	}
	private void delCurrentCmd()
	{
		CommandPanel p = getActiveCommandPanel();
		CommandData cmdToDel = p.commandData;

		switchActiveCommand( getCommandPanel( cmdToDel.getParent().cmd ) );

		p.item.dispose();
		p.comp.dispose();

		commandPanelMap.remove( p.command );

		cmdToDel.removeSelf();
	}
	private void saveCurrentDefaults()
	{
		if ( !defaultsNameBox.isOk() ) return;

		String newTitle = defaultsNameBox.getResult();

		CommandData data = getActiveCommandPanel().commandData;
		CommandData parentData = data.getParent().hasParent() ?
				data.getParent() : data;

		app.makeNewCommand( parentData.cmd, data.cmd, newTitle );
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


	public void onCommandStarted( String line )
	{
		getActiveCommandPanel().setStatus( "Running" );
		getActiveCommandPanel().setCommandLine( line );
		getActiveCommandPanel().setCommandRunning( true );
		getActiveCommandPanel().outputText.setText( "" );
	}


	public void onCommandStopped()
	{
		getActiveCommandPanel().stopButton.setEnabled( false );
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
			public void visualValidated( ValidationEvent e ) {
				// TODO: Hack warning
				if ( getActiveCommandPanel() != null ) {;
					getActiveCommandPanel().setValidationResult( e.sender, e.entries );
				}
			} };


			VIS_FACTS = new VisFact[] {
					new VisFact<>( VisualTypes.GROUP_TYPE, CompositeVisual.class ),
					new VisFact<>( VisualTypes.INT_TYPE, IntVisual.class ),
					new VisFact<>( VisualTypes.STRING_TYPE, StringVisual.class ),
					new VisFact<>( VisualTypes.COMMAND_TYPE, CommandVisual.class ),
					new VisFact<>( VisualTypes.NULL_TYPE, NullVisual.class ),
					new VisFact<>( VisualTypes.OPT_TYPE, OptVisual.class ),
					new VisFact<>( VisualTypes.ALT_TYPE, AltVisual.class ),
					new VisFact<>( VisualTypes.CONST_TYPE, ConstVisual.class )
			};
	}
}



