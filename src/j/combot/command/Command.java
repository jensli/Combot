package j.combot.command;

import j.combot.gui.visuals.CommandVisual;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

public class Command extends CommandPart<String>
{
//	private String command, title, description;
	private List<Arg<?>> args;
//	private CommandVisual visual;

	public Command( String title, String command, Arg<?>... args )
	{
		super( title, command, new CommandVisual() );
		this.args = Lists.newArrayList( args );
//		this.visual = new CommandVisual();
	}

	public List<String> getArgStrings()
	{
		List<String> argStrings = new ArrayList<String>();
		for ( Arg<?> arg : args ) {
			for ( String s : arg.getArgString() ) {
				argStrings.add( s );
			}
		}
		return argStrings;
	}

//	public CommandVisual getVisual() {
//		return visual;
//	}

	public List<Arg<?>> getArgs() {
		return args;
	}

	@Override
	public String toString() {
		return "<" + getName() + " " + args + ">";
	}




}
