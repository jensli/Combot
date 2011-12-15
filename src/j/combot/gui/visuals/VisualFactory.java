package j.combot.gui.visuals;

import j.combot.command.Arg;
import j.combot.gui.VisFact;
import j.combot.gui.misc.ValidationListener;
import j.util.util.Asserts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class VisualFactory
{
    // Remember to add fields to copy!

	private Map<VisualType<?>, VisFact<?>> map = new HashMap<>();
	private List<ValidationListener> validationListeners = new ArrayList<>( 1 );

	public VisualFactory copy()
    {
        VisualFactory result = new VisualFactory();

        // Give copy same map but unmod, we dont plan to change it.
        result.map = Collections.unmodifiableMap( map );
        result.validationListeners = new ArrayList<>( validationListeners );
        return result;
    }

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

    public void removeValidationListener( ValidationListener l ) {
        validationListeners.remove( l );
    }


	public <T> void add( VisualType<T> type, VisFact<T> fact ) {
		map.put( type, fact );
	}

	/**
	 * Creates a Visual that should reprenent the arg. Sets the visual in the
	 * arg and adds a validation listener, returns the new Visual.
	 *
	 * @param arg
	 * @return
	 */
	public <T> GuiArgVisual<T> make( Arg<T> arg )
	{
		@SuppressWarnings( "unchecked" )
		VisFact<T> visualFact = (VisFact<T>) map.get( arg.getVisualType() );

		Asserts.state( visualFact != null, "No visual factory for VisualType " + arg.getVisualType() );

		GuiArgVisual<T> v = visualFact.make();

		v.setArg( arg );
		arg.setVisual( v );

		for ( ValidationListener l : validationListeners ) {
			v.addValidationListener( l );
		}

		return v;
	};

}







