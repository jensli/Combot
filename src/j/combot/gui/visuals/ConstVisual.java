package j.combot.gui.visuals;

import j.combot.command.Arg;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class ConstVisual extends BaseArgVisual<Object> {

	@Override
	public Object getValue() {
		return getArg().getDefaultValue();
	}

	@Override
	public void makeWidget( Arg<Object> arg, Composite parent, Button parentLabel, VisualFactory visualFactory )
	{
		String title = arg.getTitle();
		makeTitle( parent, parentLabel, title );

//		// Take up one more line with two columns
//		new Label( parent, NONE );
//		new Label( parent, NONE );
	}
}
