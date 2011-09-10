package j.combot.command;

import j.combot.gui.visuals.VisualTypes;
import j.combot.validator.ValEntry;
import j.util.util.NotImplementedException;
import j.util.util.Util;

import java.util.List;

public class Command extends Arg<String>
{
	private CompositeArg children;

	public Command( String title, String command, Arg<?>... args )
	{
		super( title, command );
		setVisualType( VisualTypes.COMMAND_TYPE );
		children = new CompositeArg( "cmd comp", args );
	}

	@Override
	public String toString() {
		return Util.simpleToString( this, getTitle(), children.getChildren() );
	}

	@Override public List<ValEntry> validate() {
		throw new NotImplementedException();
	}

	// Reimplement clone even if there are no fields,
	@Override public Command clone() {
		return (Command) super.clone();
	}

	public ArgGroup getArgGroup() {
		return children.getArgGroup();
	}



}
