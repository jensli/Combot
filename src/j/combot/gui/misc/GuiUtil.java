package j.combot.gui.misc;

import j.combot.command.Arg;
import j.combot.gui.visuals.VisualFactory;

import org.eclipse.swt.widgets.Composite;

public final class GuiUtil
{
	private GuiUtil() {}


	@SuppressWarnings( { "unchecked", "rawtypes" } )
	public static void createVisual( Arg<?> arg, Composite parent, VisualFactory visualFactory )
	{
		visualFactory.make( arg ).makeWidget( (Arg) arg, parent, null, visualFactory );
	}




}
