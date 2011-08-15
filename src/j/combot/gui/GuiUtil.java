package j.combot.gui;

import j.combot.command.Arg;
import j.combot.gui.visuals.GuiArgVisual;
import j.combot.gui.visuals.ArgVisual;
import j.combot.gui.visuals.VisualFactory;

import java.util.List;

import org.eclipse.swt.widgets.Composite;

public final class GuiUtil {
	private GuiUtil() {}

	public static void createVisuals( List<Arg<?>> args, Composite comp,
			VisualFactory visualFactory )
	{
		for ( Arg<?> arg : args ) {
			createVisual( arg, comp, visualFactory);
		}
	}

	@SuppressWarnings( { "unchecked", "rawtypes" } )
	public static void createVisual( Arg<?> arg, Composite comp, VisualFactory visualFactory )
	{
		GuiArgVisual<?> visual = visualFactory.make( arg.getVisualType() );
		visual.setCommandPart( (Arg) arg );
		arg.setVisual( (ArgVisual) visual );
		visual.makeWidget( (Arg) arg, comp, visualFactory );
	}


}
