package j.combot.gui.visuals;

import j.combot.command.Arg;
import j.combot.command.CommandPart;

import org.eclipse.swt.widgets.Composite;


//public interface PartVisual<T>
public abstract class PartVisual<T>
{
	public abstract void makeWidget( CommandPart<T> part, Composite parent );
	public abstract T getValue();

	public Arg<T> cast( CommandPart<T> part ) {
		return (Arg<T>) part;
	}

}
