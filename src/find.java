import j.combot.command.AltArg;
import j.combot.command.Command;
import j.combot.command.CommandFactory;
import j.combot.command.CompositeArg;
import j.combot.command.ConstArg;
import j.combot.command.IntArg;
import j.combot.command.OptArg;
import j.combot.command.StringArg;


public class find implements CommandFactory {

	public Command make() {
		return new Command( "Find command", "find",
					new AltArg( "Symbolic link treatment", 1,
							new CompositeArg( "Titt",
									new StringArg( "Tja", "-tjo" ),
									new IntArg( "Hm", "-ha" ) ),
							new StringArg( "Woo", "-woo", "" ),
							new ConstArg( "Never follow links", "-P" ),
							new ConstArg( "Follow links", "-L" ),
							new ConstArg( "Only in args", "-H" ) ),
					new StringArg( "Location", "", "/" ),
					new OptArg( false, new ConstArg( "Limit depth 1", "-depth" ) ),
					new StringArg( "Name", "-name", "*" ),
					new OptArg( true,
						new CompositeArg( "Limit depth 2",
							new StringArg( "Search depth 1", "-maxdepth", "0" ),
							new IntArg( "Search depth 2", "-maxdepth", 0, Integer.MAX_VALUE, 10 ) ) )
		);
	}
}
