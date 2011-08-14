package j.combot;

import j.combot.command.Arg;
import j.combot.command.CommandPart;


public class CmdUtil {
	private CmdUtil() {}

	public static <T> Arg<T> cast( CommandPart<T> part ) {
		return (Arg<T>) part;
	}

	@SuppressWarnings( "unchecked" )
	public static <T, S> T cast( S part ) {
		return (T) part;
	}

}
