package j.combot.gui.visuals;

import j.combot.command.Arg;
import j.combot.gui.misc.ValidationListener;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public interface GuiArgVisual<T> extends ArgVisual<T>
{
	public void makeWidget( Arg<T> part, Composite parent, Composite childrenParent, VisualFactory visualFactory );

	public void makeChildWidgets( Arg<T> part, Composite parent, VisualFactory visualFactory );

	public void addValidationListener( ValidationListener l );
	public Control getValueControl();

}
