package j.combot.gui.visuals;

import static org.eclipse.swt.SWT.FILL;
import static org.eclipse.swt.SWT.LEFT;
import j.combot.command.CommandPart;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;



public abstract class BasePartVisual<T> extends PartVisual<T>
{
	public BasePartVisual() {
		// TODO: For prototyping, remove
	}

	@Override
	public void makeWidget( CommandPart<T> arg, Composite parent )
	{
		Label label = new Label( parent, LEFT );
//		label.setLayoutData( new GridData( ) )

		label.setText( arg.getTitle() + ":" );

		Control c = makeValueWidget( arg, parent, parent );

		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = FILL;
		c.setLayoutData( gridData );
	}



	protected Control makeValueWidget( CommandPart<T> arg, Composite parent, Composite pair )
	{
		return null;
	}


}
