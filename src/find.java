import j.combot.command.Command;
import j.combot.command.CommandFactory;
import j.combot.command.CompositeArg;
import j.combot.command.specialized.AltArg;
import j.combot.command.specialized.ConstArg;
import j.combot.command.specialized.ExtraArg;
import j.combot.command.specialized.FileArg;
import j.combot.command.specialized.FileArg.DialogType;
import j.combot.command.specialized.FileArg.Val;
import j.combot.command.specialized.IntArg;
import j.combot.command.specialized.OptArg;
import j.combot.command.specialized.Sep;
import j.combot.command.specialized.StringArg;


public class find implements CommandFactory {

	public Command make()
	{
		return new Command( "Find command", "find",
//					new CollapsableArg( "Advanced",
						new AltArg( "Symbolic link treatment", 1,
	//							new CompositeArg( "Titt",
	//									new StringArg( "Tja", "-tjo" ),
	//									new IntArg( "Hm", "-ha" ) ),
	//							new StringArg( "Woo", "-woo", "" ),
								new ConstArg( "Never follow links", "-P" ),
								new ConstArg( "Follow links", "-L" ),
								new ConstArg( "Only in args", "-H" ) ),
//		)
					new Sep(),

					new FileArg( "Location", "", "/", DialogType.DIR, Val.ERR_NO_EX ),

					new Sep(),

					new StringArg( "Name", "-name", "*" ),

					new OptArg( false,
						new CompositeArg( "Limit depth 2",
							new IntArg( "Search depth", "-maxdepth", 0, Integer.MAX_VALUE, 10 ) ) ),

					new OptArg( false, new ExtraArg( "Extra arguments" ) )
		);
	}
}
