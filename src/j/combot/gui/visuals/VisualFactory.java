package j.combot.gui.visuals;

import j.combot.gui.VisFactEntry;

import java.util.HashMap;
import java.util.Map;



public class VisualFactory {

//	@SuppressWarnings( "rawtypes" )
	private Map<VisualType<?>, VisFact<?>>
		map = new HashMap<VisualType<?>, VisFact<?>>();

	@SuppressWarnings( { "unchecked", "rawtypes" } )
	public void addAll( VisFactEntry<?>[] facts )
	{
		for ( VisFactEntry<?> e : facts ) {
			add( (VisualType) e.type, (VisFact) e.fact );
		}
	}

	public <T> void add( VisualType<T> type, VisFact<T> fact ) {
		map.put( type, fact );
	}


	public <T> GuiPartVisual<T> make( VisualType<T> type )
	{
		VisFact<?> visualFact = map.get( type );

		@SuppressWarnings( "unchecked" )
		GuiPartVisual<T> v = (GuiPartVisual<T>)  visualFact.make();
		return v;
	};
}
