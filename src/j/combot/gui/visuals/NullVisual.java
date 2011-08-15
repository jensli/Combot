package j.combot.gui.visuals;

import j.combot.command.CommandPart;

import org.eclipse.swt.widgets.Composite;

public class NullVisual extends BasePartVisual<String>
{
	@Override
	public void makeWidget( CommandPart<String> arg,
			Composite parent, VisualFactory visualFactory )
	{
		;
	}

	@Override
	public String getValue() {
		throw new UnsupportedOperationException();
	}

}
