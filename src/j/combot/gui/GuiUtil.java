package j.combot.gui;

import j.combot.command.Arg;
import j.combot.gui.visuals.GuiPartVisual;
import j.combot.gui.visuals.PartVisual;
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
		GuiPartVisual<?> visual = visualFactory.make( arg.getVisualType() );
		visual.setCommandPart( (Arg) arg );
		arg.setVisual( (PartVisual) visual );
		visual.makeWidget( (Arg) arg, comp, visualFactory );
	}


}
