import j.combot.command.Command;
import j.combot.command.CommandFactory;
import j.combot.command.CompositeArg;
import j.combot.command.specialized.AltArg;
import j.combot.command.specialized.ConstArg;
import j.combot.command.specialized.ExtraArg;
import j.combot.command.specialized.FileArg;
import j.combot.command.specialized.IntArg;
import j.combot.command.specialized.OptArg;
import j.combot.command.specialized.Sep;
import j.combot.command.specialized.StringArg;
import j.combot.validator.FileValidator.FileVal;
import j.util.util.FileOrDir;


public class find implements CommandFactory {

	public Command make()
	{
		return new Command( "Find command", "find",
					new Sep(),

					new FileArg( "Location", "", "/", FileOrDir.DIR, FileVal.ERROR_NOT_EXIST ),

					new Sep(),

					new StringArg( "Name", "-name", "*" ),

					new OptArg( false,
						new CompositeArg( "Limit depth 2",
							new IntArg( "Search depth", "-maxdepth", 0, Integer.MAX_VALUE, 10 ) ) ),

					new OptArg( false, new ExtraArg( "Extra arguments" ) ),

//					new CollapsableArg( "Advanced",
						new AltArg( "Symbolic link treatment", 1, 10,
	//							new CompositeArg( "Titt",
	//									new StringArg( "Tja", "-tjo" ),
	//									new IntArg( "Hm", "-ha" ) ),
	//							new StringArg( "Woo", "-woo", "" ),
								new ConstArg( "Never follow links", "-P" ),
								new ConstArg( "Follow links", "-L" ),
								new ConstArg( "Only in args", "-H" ) )
//		)

		);
	}
}
