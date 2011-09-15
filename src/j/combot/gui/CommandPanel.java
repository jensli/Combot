package j.combot.gui;

import static org.eclipse.swt.SWT.BEGINNING;
import static org.eclipse.swt.SWT.BORDER;
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
import j.combot.gui.visuals.GuiArgVisual;
import j.combot.gui.visuals.VisualFactory;
import j.combot.validator.ValEntry;
import j.swt.util.SwtStdValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.google.common.collect.Iterables;

class CommandPanel {

	public Composite comp;
	public TreeItem item;
	public CombotApp app;

	CommandData commandData;
	private Map<Arg<?>, List<ValEntry>> errorMap = new HashMap<>();
	private Label status;
	Label command;
	Button stopButton;
	private Button startButton;
	private Composite controls;

	StyledText outputText;

	public void addValidateResults( Iterable<ValEntry> ves ) {
		for ( ValEntry e : ves ) addValidateResult( e );
	}

	public CommandPanel( CombotApp app ) {
		this.app = app;
	}


	public void addValidateResult( ValEntry ve )
	{
		List<ValEntry> entries = errorMap.get( ve.sender );

		if ( entries == null ) {
			entries = new ArrayList<>();
			errorMap.put( ve.sender, entries );
		}

		entries.add( ve );
	}

	private void clearValidateResults( Arg<?> arg ) {
		errorMap.remove( arg );
	}

	private boolean hasValidateErrors( Arg<?> arg ) {
		return errorMap.containsKey( arg );
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

	public void setValidationResult( Arg<?> sender, List<ValEntry> entries )
	{

		if ( entries.isEmpty() ) {
		    clearValidateResults( sender );
		} else {
			addValidateResults( entries );
		}

		startButton.setEnabled( errorMap.isEmpty() );

		String toolTip = errorMap.isEmpty() ?  "" : "Can not start because of errors";
		for ( ValEntry en : Iterables.concat( errorMap.values() ) ) {
			toolTip += "\n" + en.sender.getTitle() + ": " + en.message;
		}

		controls.setToolTipText( toolTip );
	}


	@SuppressWarnings( { "rawtypes", "unchecked" } )
	public Composite makeCommandPanel( Command cmd, Composite parent, VisualFactory visualFactory )
	{
		Composite commandPanel = new Composite( parent, NONE );
		commandPanel.setLayoutData( new GridData( FILL, FILL, true, false ) );

		commandPanel.setLayout( new GridLayout( 1, false ) );

		SwtStdValues.setDebugColor( commandPanel, SwtStdValues.COLOR_DARK_GREEN );

		// Commmand title
		GuiArgVisual<?> commandVisual = visualFactory.make( cmd );
		commandVisual.makeWidget( (Arg) cmd, commandPanel, visualFactory );

		Label comArgSep = new Label( commandPanel, SEPARATOR | HORIZONTAL );
		comArgSep.setLayoutData( new GridData( FILL, TOP, true, false, 2, 1 ) );

		// Scrolling
		ScrolledComposite scrolled = new ScrolledComposite( commandPanel, V_SCROLL );
		scrolled.setExpandHorizontal( true );
		scrolled.setShowFocusedControl( true );
		scrolled.setAlwaysShowScrollBars( true );
		scrolled.setLayoutData( new GridData( FILL, FILL, true, true) );

		SwtStdValues.setDebugColor( scrolled, SwtStdValues.COLOR_DARK_BLUE );

		// Args
		Composite argsComp = new Composite( scrolled, NONE );
		scrolled.setContent( argsComp );

		SwtStdValues.setDebugColor( argsComp, SwtStdValues.COLOR_DARK_YELLOW );


		GridLayout argsLayout = new GridLayout( 2, false );
		argsComp.setLayout( argsLayout );

		// Add the acctual args
		for ( Arg<?> arg : cmd.getArgGroup() ) {
			visualFactory.make( arg ).makeWidget( (Arg) arg, argsComp, visualFactory );
		}


		Label argButtonSep = new Label( commandPanel, SEPARATOR | HORIZONTAL );
		argButtonSep.setLayoutData( new GridData( FILL, TOP, true, false ) );

		Composite controlPanel = makeControlPanel( commandPanel );
		controlPanel.setLayoutData( new GridData( FILL, FILL, true, true ) );

		argsComp.pack();
		scrolled.pack();
		commandPanel.pack();

		return commandPanel;
	}

	public void setCommandLine( String line ) {
		command.setText( "Command line: " + line );
	}

	public void setCommandRunning( boolean b )
	{
		stopButton.setEnabled( b );
		startButton.setEnabled( !b );
	}

	public void setStatus( String s ) {
		status.setText( "Status: " + s );
	}


}