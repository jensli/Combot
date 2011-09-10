import j.combot.command.Command;
import j.combot.command.CommandFactory;
import j.combot.command.ComposedArg;
import j.combot.command.IntArg;
import j.combot.command.OptArg;
import j.combot.command.StringArg;


public class ls implements CommandFactory {

	public Command make() {
		return new Command( "List command", "ls",
				new StringArg( "Pattern", "" ),
				new OptArg( true, new StringArg( "Hejj", "hej" ) ),
				new OptArg( true,
						new ComposedArg( "Some group",
								new StringArg( "Hej", "hej" ),
								new IntArg( "Int", "int" )
						)
				)
		);
	}

}
