package j.combot.gui.visuals;

import j.combot.command.Arg;
import j.combot.gui.misc.ValidationListener;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public interface GuiArgVisual<T> extends ArgVisual<T>
{
	public void makeWidget( Arg<T> part, Composite parent, VisualFactory visualFactory );
	public void makeWidget( Arg<T> part, Composite parent, Button label, VisualFactory visualFactory );

	public void addValidationListener( ValidationListener l );

}
