package j.combot.command;

import j.combot.gui.visuals.VisualTypes;
import j.util.util.NotImplementedException;
import j.util.util.Util;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

public class Command extends CommandPart<String>
{
	private List<Arg<?>> args;

	public Command( String title, String command, Arg<?>... args )
	{
		super( title, command );
		this.args = Lists.newArrayList( args );
		setVisualType( VisualTypes.STD_COMMAND_TYPE );
	}

	public List<String> getArgStrings()
	{
		List<String> argStrings = new ArrayList<String>(args.size());
		for ( Arg<?> arg : args ) {
			argStrings.addAll( arg.getArgStrings() );
		}
		return argStrings;
	}

	public List<Arg<?>> getArgs() {
		return args;
	}

	@Override
	public String toString() {
		return Util.simpleToString( this, getName(), args );
	}

	@Override public List<ValEntry> validate() {
		throw new NotImplementedException();
	}
}
