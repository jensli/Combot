package j.combot.gui.misc;

import j.combot.command.Arg;
import j.combot.gui.visuals.ArgVisual;
import j.combot.gui.visuals.GuiArgVisual;
import j.combot.gui.visuals.VisualFactory;

import org.eclipse.swt.widgets.Composite;

public final class GuiUtil
{
	private GuiUtil() {}


	@SuppressWarnings( { "unchecked", "rawtypes" } )
	public static void createVisual( Arg<?> arg, Composite comp, VisualFactory visualFactory )
	{
		GuiArgVisual<?> visual = visualFactory.make( arg );
		visual.setArg( (Arg) arg );
		arg.setVisual( (ArgVisual) visual );
		visual.makeWidget( (Arg) arg, comp, visualFactory );
	}




}
