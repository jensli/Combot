package j.combot.gui;

import j.combot.gui.visuals.VisFact;
import j.combot.gui.visuals.VisualType;

public class VisFactEntry<T> {
	public VisualType<T> type;
	public VisFact<T> fact;
	public VisFactEntry( VisualType<T> type, VisFact<T> fact ) {
		this.type = type;
		this.fact = fact;
	}
}