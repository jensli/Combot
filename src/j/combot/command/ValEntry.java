package j.combot.command;

import j.util.util.Util;


public class ValEntry
{
	public String
		message;

	public Arg<?> sender;

	public ValEntry( String message, Arg<?> sender ) {
		this.message = message;
		this.sender = sender;
	}

	public ValEntry( String message ) {
		this( message, null );
	}

	@Override
	public boolean equals( Object obj )
	{
		ValEntry e = (ValEntry) obj;
		return Util.twinEquals( message, e.message, sender, e.sender );
	}

}