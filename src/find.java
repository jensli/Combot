import j.combot.command.Command;
import j.combot.command.CommandFactory;
import j.combot.command.CompositeArg;
import j.combot.command.ConstArg;
import j.combot.command.OptArg;
import j.combot.command.StringArg;


public class find implements CommandFactory {

	public Command make() {
		return new Command( "Find command", "find",
					new StringArg( "Location", "", "/" ),
					new OptArg( false, new ConstArg( "Limit depth", "", "-depth" ) ),
					new StringArg( "Name", "-name", "*" ),
					new OptArg( true, new CompositeArg( "Limit depth",
							new StringArg( "Search depth", "-maxdepth", "0" ) ) )
//							new IntArg( "Search depth", "-maxdepth", 0, Integer.MAX_VALUE, 10 ) )
		);
	}
}
