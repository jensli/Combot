package j.combot.command.specialized;

import j.combot.command.Arg;
import j.combot.command.ArgGroup;
import j.combot.gui.visuals.VisualTypes;
import j.combot.validator.ValEntry;
import j.util.prefs.PrefNodeCollection;

import java.util.List;


public class AltArg extends Arg<Integer>
{
	@PrefNodeCollection
	private ArgGroup children = new ArgGroup();

	public AltArg( String title, int def, Arg<?>... children )
	{
		this( title, def, 0, children );
	}

	public AltArg( String title, int def, int prio, Arg<?>... children ) {
		super( title, "AltArg" );
		this.children.addAll( children );
		setDefaultValue( def );
		setVisualType( VisualTypes.ALT_TYPE );
		setPrio( prio );
	}



	public ArgGroup getChildren() {
		return children;
	}

	@Override
	public List<String> getArgStrings()
	{
		return getActiveArg().getArgStrings();
	}

	public Arg<?> getActiveArg() {
		return children.get( getVisual().getValue() );
	}

	@Override
	public List<ValEntry> validate() {
		return getActiveArg().validate();
	}


	@Override
	public AltArg clone()
	{
		AltArg cl = (AltArg) super.clone();
		cl.children = children.clone();
		return cl;
	}


}