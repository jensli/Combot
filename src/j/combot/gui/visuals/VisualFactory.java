package j.combot.gui.visuals;

import j.combot.command.Arg;
import j.combot.gui.VisFact;
import j.combot.gui.misc.ValidationListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class VisualFactory
{
	private Map<VisualType<?>, VisFact<?>> map = new HashMap<>();

	private List<ValidationListener> validationListeners = new ArrayList<>( 1 );

	@SuppressWarnings( { "unchecked", "rawtypes" } )
	public void addAll( VisFact<?>[] facts )
	{
		for ( VisFact<?> e : facts ) {
			add( (VisualType) e.getType(), e );
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
		@SuppressWarnings( "unchecked" )
		VisFact<T> visualFact = (VisFact<T>) map.get( arg.getVisualType() );

		GuiArgVisual<T> v = visualFact.make();

		v.setArg( arg );
		arg.setVisual( v );

		for ( ValidationListener l : validationListeners ) {
			v.addValidationListener( l );
		}

		return v;
	};
}







