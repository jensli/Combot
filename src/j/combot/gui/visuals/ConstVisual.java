package j.combot.gui.visuals;

import static org.eclipse.swt.SWT.FILL;
import static org.eclipse.swt.SWT.LEFT;
import static org.eclipse.swt.SWT.NONE;
import j.combot.command.Arg;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ConstVisual extends BaseArgVisual<Object> {

	@Override
	public Object getValue() {
		return getArg().getDefaultValue();
	}


	@Override
	public void makeWidget( Arg<Object> arg, Composite parent, Button parentLabel, VisualFactory visualFactory )
	{
		String title = arg.getTitle();

		if ( parentLabel != null ) {
			// The parent has already created a control where we can display our
			// title. (parent e.g. OptVisual or AltVisual)
			parentLabel.setText( title );
			((GridData) parentLabel.getLayoutData()).horizontalSpan = 2;
//			((GridData) parentLabel.getLayoutData()).verticalSpan = 2;
		} else {
			Label label = new Label( parent, NONE );
			label.setText( title );
			label.setLayoutData( new GridData( LEFT, FILL,  false, false, 2, 1) );
		}
		new Label( parent, NONE );
		new Label( parent, NONE );
	}
}
