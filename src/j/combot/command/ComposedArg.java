package j.combot.command;

import java.util.Arrays;
import java.util.List;


public abstract class ComposedArg<T> extends Arg<T>
{
	public ComposedArg( String title, String name, Arg<?>... args )
	{
		super( title, name );
		addChildren( Arrays.asList( args ) );
	}

	public ArgGroup getArgGroup() {
		return new ArgGroup( getChildren() );
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

}
