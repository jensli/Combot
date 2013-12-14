import j.combot.command.Command;
import j.combot.command.CommandFactory;
import j.combot.command.CompositeArg;
import j.combot.command.PipeCommand;
import j.combot.command.specialized.EnumArg;
import j.combot.command.specialized.EnumArg.Entry;
import j.combot.command.specialized.ExtraArg;
import j.combot.command.specialized.FileArg;
import j.combot.command.specialized.IntArg;
import j.combot.command.specialized.OptArg;
import j.combot.command.specialized.StringArg;
import j.combot.validator.FileValidator.FileVal;
import j.util.util.FileOrDir;

public class find_grep implements CommandFactory {

    public Command make()
    {
        return
            new PipeCommand( "Find Grep", "find | grep",
                new Command( "Find command", "find",
                    new FileArg( "Location", "", "/", FileOrDir.DIR, FileVal.ERROR_NOT_EXIST ),

                    new StringArg( "Name", "-name", "*" ),

                    new OptArg( false,
                        new CompositeArg( "Limit depth",
                            new IntArg( "Search depth", "-maxdepth", 0, Integer.MAX_VALUE, 10 ) ) ),

                    new OptArg( false, new ExtraArg( "Extra arguments" ) ),

                    new EnumArg( "Symbolic link treatment", 10,
                        new Entry( "Never follow links", "-P" ),
                        new Entry( "Follow links", "-L" ),
                        new Entry( "Only follow links in args", "-H" ) ) ),

                new Command( "Grep 1", "grep",
                    new StringArg( "Pattern 1", "" ) ),
                new Command( "Grep 2", "grep",
                    new StringArg( "Pattern 2", "" ) )
                );
    }
}
