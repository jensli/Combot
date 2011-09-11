package j.combot.gui;

import j.combot.command.Command;
import j.util.functional.Fun1;
import j.util.util.Util;

public class CommandData extends j.util.tree2.TreeItem<Command, CommandData>
{
	public final Command cmd;

	public CommandData( Command cmd ) {
		this.cmd = cmd;
	}

	/**
	 * To contruct the root and set the keyFun. It will then be propageted
	 * to every child and their children.
	 */
	public CommandData( Fun1<CommandData, Command> keyFun ) {
		super( keyFun );
		cmd = null;
	}

	@Override public String toString() {
		return Util.simpleToString( this, cmd == null ? "no cmd" : cmd.getTitle() );
	}
}