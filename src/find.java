import j.combot.command.Command;
import j.combot.command.CommandFactory;
import j.combot.command.ComposedArg;
import j.combot.command.ConstArg;
import j.combot.command.IntArg;
import j.combot.command.OptArg;
import j.combot.command.StringArg;


public class find implements CommandFactory {

	public Command make() {
		return new Command( "Find command", "find",
					new StringArg( "Location", "", "/" ),
					new OptArg( "Depth first", false, new ConstArg( "", "", "-depth" ) ),
					new StringArg( "Name", "-name", "*" ),
					new ComposedArg( "Group",
							new StringArg( "Hej", "du" ),
							new IntArg( "Int-int", "int" ) ),
					new OptArg( "Limit depth", true,
							new StringArg( "Search depth", "-maxdepth", "0" ) )
//							new IntArg( "Search depth", "-maxdepth", 0, Integer.MAX_VALUE, 10 ) )
		);
	}
}
