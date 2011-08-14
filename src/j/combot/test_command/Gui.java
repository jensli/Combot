package j.combot.test_command;

import static org.eclipse.swt.SWT.BEGINNING;
import static org.eclipse.swt.SWT.BORDER;
import static org.eclipse.swt.SWT.FILL;
import static org.eclipse.swt.SWT.HORIZONTAL;
import static org.eclipse.swt.SWT.LEFT;
import static org.eclipse.swt.SWT.NONE;
import static org.eclipse.swt.SWT.PUSH;
import static org.eclipse.swt.SWT.SEPARATOR;
import static org.eclipse.swt.SWT.V_SCROLL;
import j.combot.Globals;
import j.combot.command.Arg;
import j.combot.command.Command;
import j.combot.command.IntArg;
import j.combot.gui.GuiGlobals;
import j.combot.gui.visuals.IntVisual;
import j.combot.gui.visuals.VisualFact;
import j.combot.gui.visuals.VisualFactory;
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

	private VisualFactory visualFactory = new VisualFactory();

	public Gui() {
		visualFactory.add( VisualTypes.STD_INT_TYPE, new VisualFact<Integer, IntArg>() {
			public IntVisual make() {
				return new IntVisual();
			}
		});

	}

	private BasicCaller0 startCaller = new BasicCaller0(),
		stopCaller = new BasicCaller0();

	private Notifyer<Command> activeCommand = new Notifyer<Command>( null );
	private StyledText outputText;

	@SuppressWarnings( "unused" )
	private List<Command> commands;
	private Label status;

	private volatile int transactionNr = Integer.MIN_VALUE;
	private Button stopButton;
	private Button startButton;

	private boolean isCommandRunning;


	public Notifyer<Command> getActiveCommand() {
		return activeCommand;
	}

	private int getTransactionNr() {
		return transactionNr;
	}

	private void incTransactionNr() {
		transactionNr++;
	}

	private boolean isCorrectTransaction( int nr ) {
		return nr == transactionNr;
	}


	public void init()
	{
		display = new Display();
		Display.setAppName( Globals.APP_NAME );

		GuiGlobals.initTitleFont( display );
//		shell = buildShell( display );
//		makeTestContent();
		makeTestCommand();
	}

	public void receiveOutput( final String line )
	{
		final int currTransNr = getTransactionNr();

		display.asyncExec( new Runnable() {
			public void run() {
				if ( isCorrectTransaction( currTransNr ) ) {
					outputText.append( line + "\n" );
				}
			}
		});
	}

	public void receiveError( final String line )
	{
		final int currTransNr = getTransactionNr();

		display.syncExec( new Runnable() {
			public void run() {
				if ( isCorrectTransaction( currTransNr ) ) {
					outputText.append( line + "\n" );
				}
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
		status.setText( "Terminated with exit code: " + code );
		outputText.append( "Terminated with exit code: " + code );
		setCommandRunning( false );
	}

	public BasicCaller0 getStartCaller() {
		return startCaller;
	}

	public BasicCaller0 getStopCaller() {
		return stopCaller;
	}

	public void setCommadList( List<Command> commands ) {
		this.commands = commands;
	}

	public void setActiveCommand( Command command ) {
		activeCommand.set( command );
	}

	@SuppressWarnings( "unchecked" )
	private void makeTestCommand()
	{
		shell = new Shell( display );
		shell.setText( Globals.APP_NAME );
		shell.setLayout( new FillLayout() );

		//
		// Command title
		//

		Command cmd = activeCommand.get();

		Composite page = new Composite( shell, NONE );
		GridLayout pageLayout = new GridLayout( 1, false );

		page.setLayout( pageLayout );

		cmd.getVisual().makeWidget( cmd, page );

		Label comArgSep = new Label( page, SEPARATOR | HORIZONTAL );
		comArgSep.setLayoutData( new GridData( FILL, LEFT, true, false ) );

		//
		// Part with arguments
		//

		// Scrolling
		ScrolledComposite scrolled = new ScrolledComposite( page, V_SCROLL );

		scrolled.setExpandHorizontal( true );
		scrolled.setShowFocusedControl( true );
		scrolled.setAlwaysShowScrollBars( true );

		GridData argsScrollData = new GridData();
		argsScrollData.horizontalAlignment = FILL;
		argsScrollData.grabExcessHorizontalSpace = true;
		argsScrollData.verticalAlignment = FILL;
		argsScrollData.grabExcessVerticalSpace = true;
		scrolled.setLayoutData( argsScrollData );

		// Args

		Composite argsComp = new Composite( scrolled, NONE );
		scrolled.setContent( argsComp );

		GridLayout argsLayout = new GridLayout( 2, false );
		argsComp.setLayout( argsLayout );

		// Add the acctual args
		for ( @SuppressWarnings( "rawtypes" ) Arg arg : cmd.getArgs() ) {
			arg.getVisual().makeWidget( arg, argsComp );
		}

		argsComp.pack();
		scrolled.pack();

		//
		// Control panel
		//

		Label argButtonSep = new Label( page, SEPARATOR | HORIZONTAL );
		argButtonSep.setLayoutData( new GridData( FILL, LEFT, true, false ) );

		// Start and stop buttons
		Composite controls = new Composite( page, NONE );
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

		status = new Label( page, NONE );
		status.setLayoutData( new GridData( FILL, BEGINNING, true, false ) );

		// Output text
		outputText = new StyledText( page, BORDER | V_SCROLL );
		GridData outputData = new GridData( FILL, FILL, true, true);
		outputData.minimumHeight = 100;
		outputText.setLayoutData( outputData );

		outputText.setEditable( false );


		shell.pack();
		shell.setBounds( 50, 50, 400, 450 );
	}

	public void onCommandStarted( String line )
	{
		setCommandRunning( true );
		incTransactionNr();
		status.setText( "Running command: " + line );
		outputText.setText( "" );
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
		incTransactionNr();
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


	public void dipose() {
		display.dispose();
	}
}


