package j.combot.gui;

import j.combot.gui.visuals.GuiArgVisual;
import j.combot.gui.visuals.VisualType;

/**
 * This is just a tuple.
 */
public class VisFact<T>
{
	private VisualType<T> type;
	private Class<? extends GuiArgVisual<T>> visCls;


	public VisualType<T> getType() {
		return type;
	}

	public GuiArgVisual<T> make() {
		try {
			return visCls.newInstance();
		} catch ( InstantiationException | IllegalAccessException exc ) {
			throw new RuntimeException( exc );
		}
	}

	public VisFact( VisualType<T> type, final Class<? extends GuiArgVisual<T>> cls ) {
		this.type = type;
		this.visCls = cls;
	}

}