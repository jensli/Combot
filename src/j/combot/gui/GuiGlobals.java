package j.combot.gui;

import j.combot.gui.visuals.CommandVisual;
import j.combot.gui.visuals.IntVisual;
import j.combot.gui.visuals.NullVisual;
import j.combot.gui.visuals.OptVisual;
import j.combot.gui.visuals.StringVisual;
import j.combot.gui.visuals.VisualTypes;

public final class GuiGlobals {
	private GuiGlobals() {}

	public static final VisFactEntry<?>[] VIS_FACTS = new VisFactEntry[] {
			new VisFactEntry<>( VisualTypes.INT_TYPE, IntVisual.class ),
			new VisFactEntry<>( VisualTypes.STRING_TYPE, StringVisual.class ),
			new VisFactEntry<>( VisualTypes.COMMAND_TYPE, CommandVisual.class ),
			new VisFactEntry<>( VisualTypes.NULL_TYPE, NullVisual.class ),
			new VisFactEntry<>( VisualTypes.OPT_TYPE, OptVisual.class ),
		};
}
