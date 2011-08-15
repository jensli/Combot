package j.combot.gui.visuals;

import static org.eclipse.swt.SWT.FILL;
import static org.eclipse.swt.SWT.LEFT;
import static org.eclipse.swt.SWT.NONE;
import j.combot.command.Arg;
import j.combot.command.ValEntry;
import j.combot.gui.ErrorIndicator;
import j.combot.gui.GuiGlobals;

import java.util.List;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;



public abstract class BasePartVisual<T> implements GuiArgVisual<T>
{
	private Arg<T> commandPart;
	private ErrorIndicator errorIndicator = new ErrorIndicator();

	// Use caller to tell about validation?

	public BasePartVisual() {
	}

	@Override
	public void makeWidget( Arg<T> arg, Composite parent, VisualFactory visualFactory )
	{
		Label label = new Label( parent, NONE );

		label.setText( arg.getTitle() + ":" );

		Control c = makeValueWidget( arg, parent, parent );
		c.setLayoutData( new GridData( FILL, LEFT, true, false ) );

		errorIndicator.setFont( GuiGlobals.SMALL_FONT );

		// Error indication
		new Label( parent, NONE ); // Empty label to take up a cell

		errorIndicator.makeWidget( parent );
	}


	@Override
	public void setValidateResult( List<ValEntry> errors )
	{
		if ( errors.isEmpty() ) {
			errorIndicator.clearError();
		} else {
			errorIndicator.setError( errors.get( 0 ).message );
		}
	}


	protected SelectionAdapter makeValidationListener() {
		return new SelectionAdapter() {
			@Override public void widgetSelected( SelectionEvent e ) {
				setValidateResult( getCommandPart().validate() );
			}
		};
	}


	public Arg<T> getCommandPart() {
		return commandPart;
	}

	public void setCommandPart( Arg<T> commandPart ) {
		this.commandPart = commandPart;
	}


	protected Control makeValueWidget( Arg<T> arg, Composite parent, Composite pair )
	{
		return null;
	}
}
