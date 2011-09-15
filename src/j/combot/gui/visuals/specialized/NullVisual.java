package j.combot.gui.visuals.specialized;

import j.combot.command.Arg;
import j.combot.gui.visuals.BaseArgVisual;
import j.combot.gui.visuals.VisualFactory;

import org.eclipse.swt.widgets.Composite;

public class NullVisual extends BaseArgVisual<String>
{
	@Override
	public void makeWidget( Arg<String> arg,
			Composite parent, VisualFactory visualFactory )
	{
		;
	}

	@Override
	public String getValue() {
		return getArg().getDefaultValue();
	}

}
