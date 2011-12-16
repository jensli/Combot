package j.combot.validator;

import j.combot.command.Arg;
import j.util.util.IssueType;
import j.util.util.Util;

/**
 * The result of validating a Arg. Is sent to report an error of warning or
 * info item.
 */
public class ValEntry
{
	public final String message;
	public final Arg<?> sender;

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
	public String toString() {
		return Util.simpleToString( this, sender );
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( message == null ) ? 0 : message.hashCode() );
        result = prime * result + ( ( sender == null ) ? 0 : sender.hashCode() );
        result = prime * result + ( ( type == null ) ? 0 : type.hashCode() );
        return result;
    }

    /**
     * Experiment: How is it to work with immutables?
     */
    public ValEntry substSender( Arg<?> s ) {
        return new ValEntry( message, s, type );
    }

    public ValEntry substMessage( String m ) {
        return new ValEntry( m, sender, type );
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) return true;
        if ( obj == null ) return false;
        if ( getClass() != obj.getClass() ) return false;
        ValEntry other = (ValEntry) obj;
        if ( message == null ) {
            if ( other.message != null ) return false;
        } else if ( !message.equals( other.message ) )
            return false;
        if ( sender == null ) {
            if ( other.sender != null ) return false;
        } else if ( !sender.equals( other.sender ) )
            return false;
        if ( type != other.type ) return false;
        return true;
    }
}
