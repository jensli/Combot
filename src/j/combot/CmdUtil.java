package j.combot;

import j.combot.command.Arg;


public class CmdUtil {
	private CmdUtil() {}

	public static <T> Arg<T> cast( Arg<T> part ) {
		return (Arg<T>) part;
	}

	@SuppressWarnings( "unchecked" )
	public static <T, S> T cast( S part ) {
		return (T) part;
	}

}
