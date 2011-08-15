package j.combot.gui.visuals;

import j.combot.command.Arg;

import org.eclipse.swt.widgets.Composite;

public interface GuiArgVisual<T> extends ArgVisual<T>
{
	public void makeWidget( Arg<T> part, Composite parent, VisualFactory visualFactory );
}
