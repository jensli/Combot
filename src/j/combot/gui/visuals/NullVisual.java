package j.combot.gui.visuals;

import j.combot.command.Arg;

import org.eclipse.swt.widgets.Composite;

public class NullVisual extends BaseArgVisual<String>
{
	@Override
	public void makeWidget( Arg<String> arg,
			Composite parent, Composite childrenParent, VisualFactory visualFactory )
	{
		;
	}

	@Override
	public String getValue() {
		return getArg().getDefaultValue();
	}

}
