package j.combot.command;

import j.combot.gui.visuals.VisualTypes;
import j.combot.validator.ValEntry;
import j.util.prefs.PrefNode;
import j.util.process.ProcessCallback;
import j.util.process.ProcessHandler;
import j.util.util.Pair;
import j.util.util.StringUtil;
import j.util.util.Util;

import java.io.IOException;
import java.util.List;

public class Command extends Arg<Void>
{
	@PrefNode( inline = true )
	private CompositeArg children;

	public Command( String title, String command, Arg<?>... args )
	{
		super( title, command );
		children = new CompositeArg( "cmd comp", args );
		setVisualType( VisualTypes.COMMAND_TYPE );
	}

	@Override
	public String toString() {
		return Util.simpleToString( this, getTitle(), children.getChildren() );
	}

	@Override
	public void setDefaultFromVisual() {
		children.setDefaultFromVisual();
	}

	@Override
	public List<String> getArgStrings() {
		return children.getArgStrings();
	}

	public Pair<ProcessHandler, String> createProcessHander( ProcessCallback callback ) throws IOException
	{
        List<String> args = getArgStrings();
        args.add( 0, getName() );
        return new Pair<>( new ProcessHandler( callback, args ), StringUtil.join( args, " " ) );
	}

	@Override
	public List<ValEntry> validate() {
		return children.validate();
	}

	// Reimplement clone even if there are no fields,
	@Override
	public Command clone() {
		Command clone = (Command) super.clone();
		clone.children = children.clone();
		return clone;
	}

	public ArgGroup getArgGroup() {
		return children.getArgGroup();
	}

	@Override
	public int hashCode() {
		return getTitle().hashCode();
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( this == obj ) return true;
		if ( obj == null ) return false;
		if ( getClass() != obj.getClass() ) return false;
		Command other = (Command) obj;
		return getTitle().equals( other.getTitle() );
	}


}
