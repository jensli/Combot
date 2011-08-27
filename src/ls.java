import j.combot.command.Command;
import j.combot.command.CommandFactory;
import j.combot.command.StringArg;


public class ls implements CommandFactory {

	public Command make() {
		return new Command( "List command", "ls",
				new StringArg( "Pattern", "" ) );
	}

}
