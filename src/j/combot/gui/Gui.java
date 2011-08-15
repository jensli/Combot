package j.combot.gui;

import static org.eclipse.swt.SWT.BEGINNING;
import static org.eclipse.swt.SWT.BORDER;
import static org.eclipse.swt.SWT.FILL;
import static org.eclipse.swt.SWT.HORIZONTAL;
import static org.eclipse.swt.SWT.H_SCROLL;
import static org.eclipse.swt.SWT.LEFT;
import static org.eclipse.swt.SWT.NONE;
import static org.eclipse.swt.SWT.PUSH;
import static org.eclipse.swt.SWT.SEPARATOR;
import static org.eclipse.swt.SWT.TOP;
import static org.eclipse.swt.SWT.V_SCROLL;
import j.combot.Globals;
import j.combot.command.Arg;
import j.combot.command.Command;
import j.combot.gui.visuals.GuiArgVisual;
import j.combot.gui.visuals.VisualFactory;
import j.swt.util.SwtUtil;
import j.util.caller.BasicCaller0;
import j.util.notifying.Notifyer;

import java.util.List;

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


public class Gui
{


	public static void mainHIDE( String[] args ) {
		Gui b = new Gui();
		b.run();
	}

	private Shell shell;
	private Display display;
	private Label status;
	private Label command;

	private Button stopButton;
	private Button startButton;


	private VisualFactory visualFactory = new VisualFactory();

	public Gui() {
	}

	private BasicCaller0 startCaller = new BasicCaller0(),
		stopCaller = new BasicCaller0();

	private Notifyer<Command> activeCommand = new Notifyer<Command>( null );
	private StyledText outputText;

	@SuppressWarnings( "unused" )
	private List<Command> commands;
	@SuppressWarnings( "unused" )
	private boolean isCommandRunning = false;


	public Notifyer<Command> getActiveCommand() {
		return activeCommand;
	}


	public void init()
	{
		visualFactory.addAll( GuiGlobals.VIS_FACTS );

		Display.setAppName( Globals.APP_NAME );

		display = new Display();
		GuiGlobals.init( display );

		shell = makeShell( display );

		makeGui( shell );
	}


	public Shell makeShell( Display display )
	{
		Shell shell = new Shell( display );
		shell.setText( Globals.APP_NAME );
		shell.setLayout( new FillLayout() );
		return shell;
	}

	public void receiveOutput( final String line )
	{
		display.syncExec( new Runnable() {
			public void run() {
				outputText.append( line + "\n" );
				SwtUtil.scrollToMax( outputText.getHorizontalBar() );
			}
		});
	}

	public void receiveError( final String line )
	{
		display.syncExec( new Runnable() {
			public void run() {
				SwtUtil.appendStyled( outputText, line + "\n", GuiGlobals.RED );
				SwtUtil.scrollToMax( outputText.getHorizontalBar() );
			}
		});
	}

	public void signalTerminated( final int code ) {
		display.syncExec( new Runnable() {
			public void run() {
				onHasTerminated( code );
			}
		});
	}

	public void onHasTerminated( int code )
	{
		setStatus( "Terminated with exit code " + code );
		setCommandRunning( false );
	}

	public void setCommadList( List<Command> commands ) {
		this.commands = commands;
	}

	public void setActiveCommand( Command command ) {
		activeCommand.set( command );
	}

	private Composite makeGui( Composite parent )
	{

		Composite page = new Composite( parent, NONE );
		page.setLayout( new GridLayout( 1, false ) );

		Command cmd = activeCommand.get();
		makeCommandPanel( cmd, page );

		Label argButtonSep = new Label( page, SEPARATOR | HORIZONTAL );
		argButtonSep.setLayoutData( new GridData( FILL, TOP, true, false ) );

		makeControlPanel( page );

		makeOutputPanel( page );

		return page;
	}

	@SuppressWarnings( { "rawtypes", "unchecked" } )
	private void makeCommandPanel( Command cmd, Composite page )
	{
		// Commmand title
		GuiArgVisual<?> commandVisual = visualFactory.make( cmd.getVisualType() );
		commandVisual.makeWidget( (Arg) cmd, page, null );

		Label comArgSep = new Label( page, SEPARATOR | HORIZONTAL );
		comArgSep.setLayoutData( new GridData( FILL, LEFT, true, false ) );

		// Scrolling
		ScrolledComposite scrolled = new ScrolledComposite( page, V_SCROLL );
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
	}

	// Output text
	private void makeOutputPanel( Composite parent )
	{
		outputText = new StyledText( parent, BORDER | V_SCROLL | H_SCROLL );
		GridData outputData = new GridData( FILL, FILL, true, true);
		outputData.minimumHeight = 100;
		outputText.setLayoutData( outputData );

		outputText.setEditable( false );
	}

	public Composite makeControlPanel( Composite parent )
	{
		// Start and stop buttons
		Composite controls = new Composite( parent, NONE );
		controls.setLayout( new RowLayout( HORIZONTAL ) );

		startButton = new Button( controls, PUSH );
		startButton.setText( "Start" );

		startButton.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				startCaller.call();
			}
		});

		stopButton = new Button( controls, PUSH );
		stopButton.setText( "Stop" );
		stopButton.setEnabled( false );

		stopButton.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) { stopCaller.call(); }
		});

		command = new Label( parent, NONE );
		command.setLayoutData( new GridData( FILL, BEGINNING, true, false ) );
		setCommandLine( "" );

		status = new Label( parent, NONE );
		status.setLayoutData( new GridData( FILL, BEGINNING, true, false ) );
		setStatus( "Not started" );

		return controls;
	}


	// Doesnt return until SWT is stopped
	public void run()
	{
		shell.open();

		while ( !shell.isDisposed() ) {
			if ( !display.readAndDispatch() ) {
				display.sleep();
			}
		}

		display.dispose();
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

	public void dipose() {
		display.dispose();
	}

	public BasicCaller0 getStartCaller() {
		return startCaller;
	}

	public BasicCaller0 getStopCaller() {
		return stopCaller;
	}



}


