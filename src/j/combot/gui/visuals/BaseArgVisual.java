package j.combot.gui.visuals;

import static org.eclipse.swt.SWT.FILL;
import static org.eclipse.swt.SWT.LEFT;
import static org.eclipse.swt.SWT.NONE;
import j.combot.command.Arg;
import j.combot.command.ValEntry;
import j.combot.gui.misc.ErrorIndicator;
import j.combot.gui.misc.ValidationEvent;
import j.combot.gui.misc.ValidationListener;
import j.swt.util.SwtStdValues;
import j.util.caller.GenericCaller;

import java.util.List;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;



public abstract class BaseArgVisual<T> implements GuiArgVisual<T>
{
	private Arg<T> arg;
	private ErrorIndicator errorIndicator = new ErrorIndicator();
//	private List<ValidationListener> validationListeners = new ArrayList<ValidationListener>( 1 );

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
	public void makeWidget( Arg<T> arg, Composite parent, VisualFactory visualFactory )
	{
		Label label = new Label( parent, NONE );

		label.setText( arg.getTitle() + ":" );

		valueControl = makeValueWidget( arg, parent, parent );
		valueControl.setLayoutData( new GridData( FILL, LEFT, true, false ) );

		errorIndicator.setFont( SwtStdValues.SMALL_FONT );

		// Error indication
		new Label( parent, NONE ); // Empty label to take up a cell

		Control errInd = errorIndicator.makeWidget( parent );
		errInd.setLayoutData( new GridData( FILL, LEFT, true, false ) );
	}

	@Override
	public void addValidationListener( ValidationListener l ) {
		valCaller.addListener( l );
	}

	@Override
	public void setValidateResult( List<ValEntry> errors )
	{
		if ( errors.isEmpty() ) {
			errorIndicator.clearError();
			valueControl.setToolTipText( "" );
		} else {
			errorIndicator.setError( errors.get( 0 ).message );

			String tip = "";
			for ( ValEntry e : errors ) {
				tip = tip + e.message + "\n";
			}
			// TODO: Set tooltip on validator control also?
			valueControl.setToolTipText( tip );
		}

		valCaller.call( new ValidationEvent( errors, getArg() ) );
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

	//	protected abstract Control makeValueWidget( Arg<T> arg, Composite parent, Composite pair );
	@SuppressWarnings( "static-method" )
	protected Control makeValueWidget( Arg<T> arg, Composite parent, Composite pair )
	{
		return null;
	}
}
