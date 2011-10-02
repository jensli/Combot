package j.combot.validator;

import j.combot.command.Arg;
import j.util.util.IssueType;
import j.util.util.Util;


public class ValEntry
{
	public String message;
	public Arg<?> sender;

	public final IssueType type;


	public ValEntry( String message, IssueType type ) {
		this( message, null, type );
	}

	public ValEntry( String message, Arg<?> sender, IssueType type )
	{
		this.message = message;
		this.type = type;
		this.sender = sender;
	}

	public ValEntry( String message ) {
		this( message, IssueType.ERROR );
	}

	@Override
	public boolean equals( Object obj )
	{
		ValEntry e = (ValEntry) obj;
		return Util.twinEquals( message, e.message, sender, e.sender );
	}

	@Override
	public String toString() {
		return Util.simpleToString( this, sender );
	}



}
