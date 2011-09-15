

import j.combot.command.Command;
import j.combot.command.CommandFactory;
import j.combot.command.specialized.IntArg;
import j.combot.command.specialized.StringArg;

public class grep implements CommandFactory
{
	@Override public Command make()
	{
		return
			new Command( "Grep command", "grep",
					new IntArg( "Search depth", "-d" ),
					new StringArg( "Pattern", "" )
				);
	}


	public static grep makeInstance() {
		return new grep();
	}

}
