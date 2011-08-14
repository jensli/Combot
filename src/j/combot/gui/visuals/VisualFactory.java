package j.combot.gui.visuals;

import j.combot.command.CommandPart;

import java.util.HashMap;
import java.util.Map;



public class VisualFactory {

	@SuppressWarnings( "rawtypes" ) private Map<VisualType, VisualFact>
		map = new HashMap<VisualType, VisualFact>();

	@SuppressWarnings( "unchecked" )
	public void addAll( Object[][] facts )
	{
		for ( Object[] o : facts ) {
			@SuppressWarnings( "rawtypes" )
			VisualFact fact = (VisualFact) o[1];
			@SuppressWarnings( "rawtypes" )
			VisualType type = (VisualType) o[0];

			add( type, fact );
		}
	}

	public <T,  V extends PartVisual<T>>
	void add( VisualType<T> type, VisualFact<T> fact ) {
		map.put( type, fact );
	}


	public <T, S extends CommandPart<T>,  V extends PartVisual<T>>
	V make( VisualType<T> type ) {
		@SuppressWarnings( "unchecked" )
		V v = (V) map.get( type ).make();
		return v;
	};
}
