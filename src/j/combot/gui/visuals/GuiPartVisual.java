package j.combot.gui.visuals;

import j.combot.command.CommandPart;

import org.eclipse.swt.widgets.Composite;

public interface GuiPartVisual<T> extends PartVisual<T>
{
	public void makeWidget( CommandPart<T> part, Composite parent, VisualFactory visualFactory );
}
