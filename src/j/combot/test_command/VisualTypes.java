package j.combot.test_command;

import j.combot.command.IntArg;
import j.combot.command.StringArg;
import j.combot.gui.visuals.VisualType;

public class VisualTypes {
	public static final VisualType<Integer, IntArg> STD_INT_TYPE = new VisualType<Integer, IntArg>();
	public static final VisualType<String, StringArg> STD_STRING_TYPE = new VisualType<String, StringArg>();
}
