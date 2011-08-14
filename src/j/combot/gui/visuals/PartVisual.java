package j.combot.gui.visuals;

import j.combot.command.CommandPart;

import org.eclipse.swt.widgets.Composite;


//public interface PartVisual<T>
public interface PartVisual<T, S extends CommandPart<T, S>>
{
	public void makeWidget( S part, Composite parent );
	public T getValue();

}
