package j.combot.gui;

import j.combot.command.Command;
import j.util.notifying.Notifyer;

public class GuiData {
	public Notifyer<Command> selectedCommand = new Notifyer<Command>( null );
}
