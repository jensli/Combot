package j.combot.gui;

import j.combot.command.Command;
import j.util.functional.Fun1;
import j.util.util.Util;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

public class CommandData extends j.util.tree2.TreeItem<Command, CommandData>
{
	public final Command cmd;
	public Composite page;
	public TreeItem item;

	public CommandData( Command cmd, Composite page, TreeItem item ) {
		this.cmd = cmd;
		this.page = page;
		this.item = item;
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