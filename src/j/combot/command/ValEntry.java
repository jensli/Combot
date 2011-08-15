package j.combot.command;

public class ValEntry
{
	public String
		message,
		sender;

	public ValEntry( String message, String sender ) {
		this.message = message;
		this.sender = sender;
	}

	public ValEntry( String message ) {
		this( message, null );
	}


}
