package j.combot.command;

public class ComposedArg<T> extends Arg<T>
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


}
