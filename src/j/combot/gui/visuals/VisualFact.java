package j.combot.gui.visuals;

import j.combot.command.CommandPart;

public interface VisualFact<T, S extends CommandPart<T, S>> {
	public PartVisual<T, S> make();
}
