import j.combot.command.Command;
import j.combot.command.CommandFactory;
import j.combot.command.CompositeArg;
import j.combot.command.specialized.IntArg;
import j.combot.command.specialized.OptArg;
import j.combot.command.specialized.StringArg;


public class ls implements CommandFactory {

	public Command make() {
		return new Command( "List command", "ls",
				new StringArg( "Pattern", "" ),
				new OptArg( true, new StringArg( "Hejj", "hej" ) ),
				new OptArg( true,
						new CompositeArg( "Some group",
								new StringArg( "Hej", "hej" ),
								new IntArg( "Int", "int" )
						)
				)
		);
	}

}
