package j.combot.command;

import j.combot.gui.visuals.VisualTypes;
import j.combot.validator.ValEntry;
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



	@Override
	public List<String> getArgStrings() {
		return children.getArgStrings();
	}

	@Override
	public List<ValEntry> validate() {
		return children.validate();
	}

	// Reimplement clone even if there are no fields,
	@Override
	public Command clone() {
		Command clone = (Command) super.clone();
		clone.children = children.clone();
		return clone;
	}

	public ArgGroup getArgGroup() {
		return children.getArgGroup();
	}



}
