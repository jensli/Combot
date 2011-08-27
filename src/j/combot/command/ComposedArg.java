package j.combot.command;

import java.util.List;


public abstract class ComposedArg<T> extends Arg<T>
{
	private ArgGroup args = new ArgGroup();

	public ComposedArg( String title, String name, Arg<?>... args )
	{
		super( title, name );
		this.args.addAll( args );
	}

	public ArgGroup getArgGroup() {
		return args;
	}

	@Override
	public List<String> getArgStrings()
	{
		return getArgGroup().getArgStrings();
	}




	@Override public void setDefaultFromVisual() {
		for ( Arg<?> a : args ) {
			a.setDefaultFromVisual();
		}
	}

	@Override public Arg<T> clone() {
		ComposedArg<T> res = (ComposedArg<T>) super.clone();
		res.args = args.clone();
		return res;
	}



}
