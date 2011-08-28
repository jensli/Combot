package j.combot.app;

import j.combot.command.Arg;
import j.combot.command.ArgGroup;
import j.combot.command.Command;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Iterables;

public class CommandContainer implements Iterable<Entry<Command, ArgGroup>>
{
	private Map<Command, ArgGroup> commands = new LinkedHashMap<>();

	public void addParent( Command c ) {
		commands.put( c, new ArgGroup() );
	}

	public void addChild( Command parent, Command child ) {
		commands.get( parent ).add( child );
	}

	public void addChilds( Command parent, Iterable<Command> childs ) {
		for ( Command c : childs ) addChild( parent, c );
	}

	public Map<Command, ArgGroup> getCommands() {
		return commands;
	}

	public Iterable<Arg<?>> getLinear() {
		return Iterables.concat( 
					commands.keySet(), 
					Iterables.concat( commands.values() ) );
	}

	@Override
	public Iterator<Entry<Command, ArgGroup>> iterator() {
		return commands.entrySet().iterator();
	}


}
