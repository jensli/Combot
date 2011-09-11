package j.combot.command;

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
		super( title, "AltArg" );

		this.children.addAll( children );
		setDefaultValue( def );
		setVisualType( VisualTypes.ALT_TYPE );
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



}