package j.combot.gui;

import static org.eclipse.swt.SWT.BEGINNING;
import static org.eclipse.swt.SWT.BORDER;
import static org.eclipse.swt.SWT.CHECK;
import static org.eclipse.swt.SWT.DEFAULT;
import static org.eclipse.swt.SWT.FILL;
import static org.eclipse.swt.SWT.HORIZONTAL;
import static org.eclipse.swt.SWT.H_SCROLL;
import static org.eclipse.swt.SWT.NONE;
import static org.eclipse.swt.SWT.PUSH;
import static org.eclipse.swt.SWT.SEPARATOR;
import static org.eclipse.swt.SWT.TOP;
import static org.eclipse.swt.SWT.V_SCROLL;
import j.combot.app.CombotApp;
import j.combot.command.Arg;
import j.combot.command.Command;
import j.combot.gui.misc.ValidationEvent;
import j.combot.gui.misc.ValidationListener;
import j.combot.gui.visuals.GuiArgVisual;
import j.combot.gui.visuals.VisualFactory;
import j.combot.util.SwtUtil;
import j.combot.validator.ValEntry;
import j.util.util.IssueType;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TreeItem;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

class CommandPanel
{
	private Composite mainComposite;
	private TreeItem item;
	public CombotApp app;

	private CommandData cmdData;
    private Multimap<Arg<?>, ValEntry> errorMultimap = LinkedHashMultimap.create();

	private Label status;
	private Label commandLabel;
	private Button stopButton;
	private Button startButton;
	private Composite controls;
	private StyledText outputText;
	private Button filterErrors;

    private boolean startupOngoing = true;


	public CommandPanel( CombotApp app ) {
		this.app = app;
	}

	public void addOutput( String line ) {
		outputText.append( line );
	}

	public void addErrorOutput( String line ) {
		if ( !filterErrors.getSelection() ) {
			SwtUtil.appendStyled( outputText, line, SwtUtil.COLOR_RED );
		}
	}

	public TreeItem getTreeItem() {
		return item;
	}

	public Composite getComposite() {
		return mainComposite;
	}

	private Composite makeControlPanel( Composite parent )
	{
		Composite panel = new Composite( parent, NONE );
		panel.setLayout( new GridLayout( 1, false ) );

		controls = new Composite( panel, NONE );
		RowLayout buttonLayout = new RowLayout( HORIZONTAL );
		buttonLayout.pack = false;
		buttonLayout.spacing = SwtUtil.SPACING;
		controls.setLayout( buttonLayout );

		// Start button
		startButton = new Button( controls, PUSH );
		startButton.setText( "Start" );
		startButton.setLayoutData( new RowData( SwtUtil.BUTTON_WIDTH, DEFAULT ) );

		startButton.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				app.startCmd( cmdData.cmd );
			}
        } );

		// Stop button
		stopButton = new Button( controls, PUSH );
		stopButton.setText( "Stop" );
		stopButton.setEnabled( false );

		stopButton.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				app.stopCommand();
			}
		} );

		// Filter checkbox
		filterErrors = new Button( controls, CHECK );
		filterErrors.setText( "Filter error output" );
		filterErrors.setSelection( false );

		// Status labels
		commandLabel = new Label( panel, NONE );
		commandLabel.setLayoutData( new GridData( FILL, BEGINNING, true, false ) );
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
	 * Sets the validation status of the panel, dis/enabeling start/stop buttons
	 * when there are an error and sets tooltips.
	 *
	 * Called when a widet has validated itself. And when panel created.
	 */
	private void setValidationResult( Arg<?> sender, List<ValEntry> entries )
	{
	    if ( startupOngoing ) return; // Ignore validation during startup

		List<ValEntry> errors = new ArrayList<>();

		for ( ValEntry e : entries ) {
			if ( e.type == IssueType.ERROR ) errors.add( e );
		}

		errorMultimap.replaceValues( sender, entries );

		updateControlErrorReporting();
	}

    private void updateControlErrorReporting()
    {
        startButton.setEnabled( errorMultimap.isEmpty() );

		// Set tooltop on the start/stop buttons
		String toolTip = errorMultimap.isEmpty() ?  "" : "Can not start because of errors";
        for ( ValEntry en : errorMultimap.values() ) {
			toolTip += "\n" + en.sender.getTitle() + ": " + en.message;
		}

		controls.setToolTipText( toolTip );
    }

	public void init( TreeItem item, Command cmd, CommandData parent,
			Composite commandComp, VisualFactory visualFactory )
	{
		cmdData = new CommandData( cmd );
		parent.addChild( cmdData  );

		this.item = item;

		mainComposite = makeCommandPanel( cmd, commandComp, visualFactory );

		startupOngoing = false;

		// Validate without displaying error messages, so that start button is
		// disabled if there is an error.
		for ( ValEntry e : cmd.validate() ) {
			if ( e.type == IssueType.ERROR ) errorMultimap.put( e.sender, e );
		}

		updateControlErrorReporting();
	}

	@SuppressWarnings( { "rawtypes", "unchecked" } )
	private Composite makeCommandPanel( Command cmd, Composite parent, VisualFactory visualFactory )
	{
		Composite commandPanel = new Composite( parent, NONE );
		commandPanel.setLayoutData( new GridData( FILL, FILL, true, false ) );

		// This change only affect out copy of visFact.
	    // Validation listener for all the ArgVisuals, is run every time
	    // something changes in the arg input controls.
	    // Sets enabled status for start button and tooltip.
		visualFactory.addValidationListener( new ValidationListener() {
	        public void visualValidated( ValidationEvent e ) {
	            setValidationResult( e.sender, e.entries );
	        } } );

		commandPanel.setLayout( new GridLayout( 1, false ) );

		SwtUtil.setDebugColor( commandPanel, SwtUtil.COLOR_DARK_GREEN );

		// Commmand title
		GuiArgVisual<?> commandVisual = visualFactory.make( cmd );
		commandVisual.makeWidget( (Arg) cmd, commandPanel, visualFactory );

		Label comArgSep = new Label( commandPanel, SEPARATOR | HORIZONTAL );
		comArgSep.setLayoutData( new GridData( FILL, TOP, true, false, 2, 1 ) );

		// Scrolling
		ScrolledComposite scrolled = new ScrolledComposite( commandPanel, V_SCROLL );
		scrolled.setExpandHorizontal( true );
		scrolled.setShowFocusedControl( true );
		scrolled.setLayoutData( new GridData( FILL, FILL, true, true ) );
//		scrolled.setAlwaysShowScrollBars( true );

		SwtUtil.setDebugColor( scrolled, SwtUtil.COLOR_DARK_BLUE );

		// Args
		Composite argsComp = makeArgsPanel( cmd, visualFactory, scrolled );
	    scrolled.setContent( argsComp );

		Label argButtonSep = new Label( commandPanel, SEPARATOR | HORIZONTAL );
		argButtonSep.setLayoutData( new GridData( FILL, TOP, true, false ) );

		Composite controlPanel = makeControlPanel( commandPanel );
		controlPanel.setLayoutData( new GridData( FILL, FILL, true, true ) );

		argsComp.pack();
		scrolled.pack();
		commandPanel.pack();

		return commandPanel;
	}

    @SuppressWarnings( { "unchecked", "rawtypes" } )
    private static Composite makeArgsPanel( Command cmd, VisualFactory visualFactory,
            ScrolledComposite parent )
    {
        Composite argsComp = new Composite( parent, NONE );

		SwtUtil.setDebugColor( argsComp, SwtUtil.COLOR_DARK_YELLOW );

		GridLayout argsLayout = new GridLayout( 2, false );
		argsComp.setLayout( argsLayout );

		// Add the acctual args
		for ( Arg<?> arg : cmd.getArgGroup() ) {
			visualFactory.make( arg ).makeWidget( (Arg) arg, argsComp, visualFactory );
		}

        return argsComp;
    }

	private void setCommandLine( String line ) {
		getCommandLabel().setText( "Command line: " + line );
	}

	private void setCommandRunning( boolean b )
	{
		stopButton.setEnabled( b );
		startButton.setEnabled( !b );
	}

	public void setStatus( String s ) {
		status.setText( "Status: " + s );
	}


	public void onCommandStarted( String line )
	{
		setStatus( "Running" );
		setCommandLine( line );
		setCommandRunning( true );
		outputText.setText( "" );
	}

	public void onCommandStopped()
	{
		stopButton.setEnabled( false );
	}

	public void dispose()
	{
		cmdData.removeSelf();
		item.dispose();
		mainComposite.dispose();
	}

	public Label getCommandLabel() {
		return commandLabel;
	}

	public CommandData getCommandData() {
		return cmdData;
	}

	public void onCommandTerminated( int code, String msg )
	{
	    setCommandRunning( false );

	    if ( msg != null ) {
	        setStatus( msg );
	    } else {
	        setStatus( "Terminated with exit code " + code );
	    }
	}

}
