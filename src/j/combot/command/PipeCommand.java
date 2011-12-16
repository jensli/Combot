package j.combot.command;

import j.combot.gui.visuals.VisualTypes;
import j.combot.validator.ValEntry;
import j.util.prefs.PrefNodeCollection;
import j.util.process.ProcessCallback;
import j.util.process.ProcessHandler;
import j.util.util.Pair;

import java.io.IOException;
import java.util.List;

import com.google.common.collect.Iterables;


/**
 * TODO: This whole class is an ugly hack. Should refactor Command and think
 * about how they are divided.
 */
public class PipeCommand extends Command
{
    @PrefNodeCollection
    private ArgGroup pipedCmds;

    public PipeCommand( String title, String command, Command... cmds )
    {
        super( title, command );
        pipedCmds = new ArgGroup( cmds );
        setVisualType( VisualTypes.COMMAND_TYPE );
    }

    @Override
    public Pair<ProcessHandler, String> createProcessHander( ProcessCallback callback ) throws IOException
    {
        Iterable<Command> cmds = Iterables.filter( pipedCmds, Command.class );
        Pair<ProcessHandler, String>
            first = Iterables.get( cmds, 0 ).createProcessHander( callback ),
            p;

        ProcessHandler firstProcHand = first.a,
                prevProcHand = firstProcHand;

        String cmdStr = first.b;

        for ( Command cmd : Iterables.skip( cmds, 1 ) ) {
            cmdStr += " | ";

            p = cmd.createProcessHander( callback );
            prevProcHand.setCallback( p.a.pipeCallback() );
            p.a.runAsync();

            prevProcHand = p.a;
            cmdStr += p.b;
        }

        return new Pair<>( firstProcHand, cmdStr );
    }

    @Override
    public void setDefaultFromVisual() {
        pipedCmds.setDefaultFromVisual();
    }

    @Override
    public List<String> getArgStrings() {
        return pipedCmds.getArgStrings();
    }

    @Override
    public List<ValEntry> validate() {
        return pipedCmds.validate();
    }

    // Reimplement clone even if there are no fields,
    @Override
    public Command clone() {
        PipeCommand clone = (PipeCommand) super.clone();
        clone.pipedCmds = pipedCmds.clone();
        return clone;
    }

    public ArgGroup getArgGroup()
    {
        ArgGroup result = new ArgGroup();

        for ( Arg<?> a : pipedCmds ) result.addAll( ((Command) a).getArgGroup() );

        return result;
    }

    @Override
    public int hashCode() {
        return getTitle().hashCode();
    }



}
