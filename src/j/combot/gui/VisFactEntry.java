package j.combot.gui;

import j.combot.gui.visuals.GuiArgVisual;
import j.combot.gui.visuals.VisFact;
import j.combot.gui.visuals.VisualType;

public class VisFactEntry<T> {
	public VisualType<T> type;
	public VisFact<T> fact;

	public VisFactEntry( VisualType<T> type, VisFact<T> fact ) {
		this.type = type;
		this.fact = fact;
	}

	public VisFactEntry( VisualType<T> type, final Class<? extends GuiArgVisual<T>> cls ) {
		this.type = type;
		this.fact = new VisFact<T>() {
			public GuiArgVisual<T> make() {
				try {
					return cls.newInstance();
				} catch ( InstantiationException exc ) {
					throw new RuntimeException( exc );
				} catch ( IllegalAccessException exc ) {
					throw new RuntimeException( exc );
				}
			}
		};
	}

}