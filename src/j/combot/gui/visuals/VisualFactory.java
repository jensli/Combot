package j.combot.gui.visuals;

import j.combot.command.Arg;
import j.combot.gui.VisFactEntry;
import j.combot.gui.misc.ValidationListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class VisualFactory {

//	@SuppressWarnings( "rawtypes" )
	private Map<VisualType<?>, VisFact<?>> map = new HashMap<>();

	private List<ValidationListener> validationListeners = new ArrayList<>( 1 );

	@SuppressWarnings( { "unchecked", "rawtypes" } )
	public void addAll( VisFactEntry<?>[] facts )
	{
		for ( VisFactEntry<?> e : facts ) {
			add( (VisualType) e.type, (VisFact) e.fact );
		}
	}

	public void addValidationListener( ValidationListener l ) {
		validationListeners.add( l );
	}

	public <T> void add( VisualType<T> type, VisFact<T> fact ) {
		map.put( type, fact );
	}


	public <T> GuiArgVisual<T> make( Arg<T> arg )
	{
		VisFact<?> visualFact = map.get( arg.getVisualType() );

		@SuppressWarnings( "unchecked" )
		GuiArgVisual<T> v = (GuiArgVisual<T>)  visualFact.make();

		arg.setVisual( v );
		for ( ValidationListener l : validationListeners ) {
			v.addValidationListener( l );
		}
		return v;
	};
}
