package j.combot.command;

import j.util.prefs.PrefNodeCollection;

import java.util.Collection;
import java.util.List;


public abstract class ComposedArg<T> extends Arg<T>
{
	@PrefNodeCollection
	private ArgGroup argGroup = new ArgGroup();

	public ComposedArg( String title, String name, Arg<?>... args )
	{
		super( title, name );
		argGroup.addAll( args );
	}

	public ArgGroup getArgGroup() {
		return argGroup;
	}

	public Collection<Arg<?>> getChildren() {
		return argGroup.getArgs();
	}

	@Override
	public List<String> getArgStrings()
	{
		return getArgGroup().getArgStrings();
	}

	@Override
	public void setDefaultFromVisual() {
		for ( Arg<?> a : getChildren() ) {
			a.setDefaultFromVisual();
		}
	}

	@Override
	public Arg<T> clone()
	{
		ComposedArg<T> cl = (ComposedArg<T>) super.clone();
		cl.argGroup = argGroup.clone();
		return cl;
	}


}
