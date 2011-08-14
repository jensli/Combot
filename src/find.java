import j.combot.command.Command;
import j.combot.command.CommandFactory;
import j.combot.command.IntArg;
import j.combot.command.StringArg;


public class find implements CommandFactory {

	public Command make() {
		return new Command( "Find command", "find",
					new StringArg( "Location", "", "/" ),
					new StringArg( "Name", "-name", "*" ),
					new IntArg( "Search depth", "-maxdepth", 0, Integer.MAX_VALUE, 10 ) );
	}

}
