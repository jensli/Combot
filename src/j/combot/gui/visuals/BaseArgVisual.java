package j.combot.gui.visuals;

import static org.eclipse.swt.SWT.FILL;
import static org.eclipse.swt.SWT.LEFT;
import static org.eclipse.swt.SWT.NONE;
import j.combot.command.Arg;
import j.combot.gui.misc.ErrorIndicator;
import j.combot.gui.misc.ValidationEvent;
import j.combot.gui.misc.ValidationListener;
import j.combot.util.SwtUtil;
import j.combot.validator.ValEntry;
import j.util.caller.GenericCaller;

import java.util.Collections;
import java.util.List;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;


/**
 * Default implementation with standard methods for creating widget, validation,
 * dis/enable ...
 */
public abstract class BaseArgVisual<T> implements GuiArgVisual<T>
{
	private Arg<T> arg;
	protected boolean enabled = true;

	private ErrorIndicator errorIndicator;

	// TODO: Show this be moved to Arg?
	private GenericCaller<ValidationEvent, ValidationListener> validateCaller =
			new GenericCaller<>( ValidationEvent.class, ValidationListener.class );

	// The sublass spesific control that gets the acctual input from the user,
	// i.e. not the label but the input field. Often this if the only control a
	// subclass needs to create.
	private Control valueControl;

	// Use caller to tell about validation?

	public BaseArgVisual() {
	}

	protected void setValueControl( Control c ) {
		valueControl = c;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public final void makeWidget( Arg<T> arg, Composite parent, VisualFactory visualFactory )
	{
		makeWidget( arg, parent, null, visualFactory );
	}

	@Override
	public void makeWidget( Arg<T> arg, Composite parent, Button parentLabel, VisualFactory visualFactory )
	{
		String title = arg.getTitle() + ":";

		if ( parentLabel != null ) {
			// The parent has already created a control where we can display our
			// title. (parent e.g. OptVisual or AltVisual)
			parentLabel.setText( title );
		} else {
			Label label = new Label( parent, NONE );
			label.setText( title );
		}

		valueControl = makeValueWidget( arg, parent );

		new Label( parent, NONE ); // Empty label to take up a cell

		// Error indication
		errorIndicator = new ErrorIndicator();
		errorIndicator.setFont( SwtUtil.SMALL_FONT );
		Control errInd = errorIndicator.makeWidget( parent );
		errInd.setLayoutData( new GridData( FILL, LEFT, true, false, 1, 1 ) );
	}

	/**
	 * Template method, if subclass uses this class's makeWidget method it only
	 * has to override this one.
	 */
	@SuppressWarnings( "static-method" )
	protected Control makeValueWidget( Arg<T> arg, Composite parent )
	{
		throw new UnsupportedOperationException( "Trying to call makeValueWidget on class that do not implement it" );
	}


	@Override
	public void addValidationListener( ValidationListener l ) {
		validateCaller.addListener( l );
	}

	@Override
	public void setValidateResult( List<ValEntry> errors )
	{
		if ( errors.isEmpty() ) {
			errorIndicator.clearIssue();
			valueControl.setToolTipText( "" );
		} else {
			errorIndicator.setIssue( errors.get( 0 ).type , errors.get( 0 ).message );

			String tip = "";
			for ( ValEntry e : errors ) {
			    if ( !tip.isEmpty() ) tip += "\n";
				tip += e.message;
			}
			// TODO: Set tooltip on validator control also?
			valueControl.setToolTipText( tip );
		}

		validateCaller.call( new ValidationEvent( errors, getArg() ) );
	}


	@Override
	public void setEnabled( boolean b )
	{
		enabled = b;
		if ( valueControl != null ) valueControl.setEnabled( b );
		if ( errorIndicator != null ) errorIndicator.setEnabled( b );

		// Send a Validation event, to send errors if we are enabling,
		// or to clear error if we are disabeling.

        // History: Here was OptArg.validate was called, calling its child, but
        // OptArg set as sender. This bug was due to a too entangled class
        // hierarcy. Should refactor.
		validateCaller.call(
				new ValidationEvent(
						b ? getArg().validate() : Collections.<ValEntry>emptyList(),
						getArg() ) );
	}

	protected SelectionAdapter makeValidationListener() {
		return new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				setValidateResult( getArg().validate() );
			}
		};
	}

	public Arg<T> getArg() {
		return arg;
	}

	public void setArg( Arg<T> arg ) {
		this.arg = arg;
	}

	public void dispose() {
		if ( valueControl != null ) valueControl.dispose();
		if ( errorIndicator != null ) errorIndicator.dispose();
	}

	protected static void makeTitle( Composite parent, Button parentLabel, String title )
	{
		if ( parentLabel != null ) {
			// The parent has already created a control where we can display our
			// title. (parent e.g. OptVisual or AltVisual)
			parentLabel.setText( title );
			((GridData) parentLabel.getLayoutData()).horizontalSpan = 2;
		} else {
			Label label = new Label( parent, NONE );
			label.setText( title );
			label.setLayoutData( new GridData( LEFT, FILL,  false, false, 2, 1) );
		}
	}

}
