package j.combot.command;

import j.combot.gui.visuals.VisualTypes;
import j.util.util.NotImplementedException;
import j.util.util.Util;

import java.util.List;

public class Command extends ComposedArg<String>
{
	public Command( String title, String command, Arg<?>... args )
	{
		super( title, command , args );
		setVisualType( VisualTypes.STD_COMMAND_TYPE );
	}

	public List<String> getArgStrings()
	{
		return getArgGroup().getArgStrings();
	}

	@Override
	public String toString() {
		return Util.simpleToString( this, getName(), getArgGroup().getArgs() );
	}

	@Override public List<ValEntry> validate() {
		throw new NotImplementedException();
	}

	@Override public Command clone() {
		return (Command) super.clone();
	}

}
