package j.combot.gui.visuals;

import static org.eclipse.swt.SWT.FILL;
import static org.eclipse.swt.SWT.LEFT;
import static org.eclipse.swt.SWT.NONE;
import j.combot.command.Arg;
import j.combot.gui.misc.ErrorIndicator;
import j.combot.gui.misc.ValidationEvent;
import j.combot.gui.misc.ValidationListener;
import j.combot.validator.ValEntry;
import j.swt.util.SwtStdValues;
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



public abstract class BaseArgVisual<T> implements GuiArgVisual<T>
{
	private Arg<T> arg;
	protected boolean enabled = true;

	private ErrorIndicator errorIndicator;

	private GenericCaller<ValidationEvent, ValidationListener> valCaller =
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
	public void makeWidget( Arg<T> arg, Composite parent, VisualFactory visualFactory )
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

		valueControl = makeValueWidget( arg, parent, parent );

		if ( valueControl != null ) {
			valueControl.setLayoutData( new GridData( FILL, FILL, false, false ) );
		}

		new Label( parent, NONE ); // Empty label to take up a cell

		// Error indication
		errorIndicator = new ErrorIndicator();
		errorIndicator.setFont( SwtStdValues.SMALL_FONT );
		Control errInd = errorIndicator.makeWidget( parent );
		errInd.setLayoutData( new GridData( FILL, LEFT, true, false, 1, 1 ) );
	}

	//	protected abstract Control makeValueWidget( Arg<T> arg, Composite parent, Composite pair );
	@SuppressWarnings( "static-method" )
	protected Control makeValueWidget( Arg<T> arg, Composite parent, Composite pair )
	{
		return null;
	}


	@Override
	public void addValidationListener( ValidationListener l ) {
		valCaller.addListener( l );
	}

	private List<ValEntry> savedErrors = Collections.emptyList();

	@Override
	public void setValidateResult( List<ValEntry> errors )
	{
		savedErrors = errors;
		if ( errors.isEmpty() ) {
			errorIndicator.clearError();
			valueControl.setToolTipText( "" );
		} else {
			errorIndicator.setError( errors.get( 0 ).message );

			String tip = "";
			for ( ValEntry e : errors ) {
				tip += e.message + "\n";
			}
			// TODO: Set tooltip on validator control also?
			valueControl.setToolTipText( tip );
		}

		valCaller.call( new ValidationEvent( errors, getArg() ) );
	}

	@Override
	public void setEnabled( boolean b )
	{
		enabled = b;
		if ( valueControl != null ) valueControl.setEnabled( b );
		if ( errorIndicator != null ) errorIndicator.setEnabled( b );

		List<ValEntry> errors;

		if ( b ) {
//			errors = savedErrors;
			errors = getArg().validate();
		} else {
			// Clear errors for listeners by sending empty list
			errors = Collections.emptyList();
		}

		valCaller.call( new ValidationEvent( errors, getArg() ) );
	}

	@Override
	public void makeChildWidgets( Arg<T> part, Composite parent, VisualFactory visualFactory ) {
		;
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
		if ( valueControl != null ) {
			valueControl.dispose();
		}
	}

	@Override
	public Control getValueControl() {
		return valueControl;
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
