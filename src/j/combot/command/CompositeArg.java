package j.combot.command;

import j.combot.gui.visuals.VisualTypes;
import j.combot.validator.ValEntry;
import j.util.prefs.PrefNodeCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class CompositeArg extends Arg<Void>
{
	@PrefNodeCollection
	private ArgGroup argGroup = new ArgGroup();

	public CompositeArg( String title, Arg<?>... children )
	{
		super( title, "Comp" );
		argGroup.addAll( children );
		setVisualType( VisualTypes.GROUP_TYPE );
	}

	public ArgGroup getArgGroup() {
		return argGroup;
	}

	public Collection<Arg<?>> getChildren() {
		return argGroup.getArgs();
	}

	@Override
	public List<ValEntry> validate() {
		return argGroup.validate();
	}

	@Override
	public List<String> getArgStrings()
	{
		List<Arg<?>> l = new ArrayList<>( argGroup.getArgs() );

		Collections.sort( l, new Comparator<Arg<?>>() {
			public int compare( Arg<?> o1, Arg<?> o2 ) {
				// Sort so that high prio is first
				return o2.getPrio() - o1.getPrio();
			}
		} );

		return ArgGroup.asArgGroup( l ).getArgStrings();
	}

	@Override
	public void setDefaultFromVisual()
	{
		for ( Arg<?> a : getChildren() ) {
			a.setDefaultFromVisual();
		}
	}

	@Override
	public CompositeArg clone()
	{
		CompositeArg cl = (CompositeArg) super.clone();
		cl.argGroup = argGroup.clone();
		return cl;
	}

}
