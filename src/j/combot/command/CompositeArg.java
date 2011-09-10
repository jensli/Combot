package j.combot.command;

import j.combot.gui.visuals.VisualTypes;
import j.util.prefs.PrefNodeCollection;

import java.util.Collection;
import java.util.List;


public class CompositeArg extends Arg<Object>
{
	@PrefNodeCollection
	private ArgGroup argGroup = new ArgGroup();

	public CompositeArg( String title, Arg<?>... children )
	{
		super( title, "Comp" );
		argGroup.addAll( children );
		setVisualType( VisualTypes.GROUP_TYPE );
	}

//	public ComposedArg( String title, Arg<?>... children )
//	{
//		this( title, "composed no name", children );
//	}

	public ArgGroup getArgGroup() {
		return argGroup;
	}

	/**
	 * Used by visuals if they want to create different widgets if there are
	 * children.
	 */
	@Override
	public boolean isSimple() {
		return false;
	}

	public Collection<Arg<?>> getChildren() {
		return argGroup.getArgs();
	}

	@Override
	public List<String> getArgStrings()
	{
		return getArgGroup().getArgStrings();
	}

	@Override
	public void setDefaultFromVisual() {
		for ( Arg<?> a : getChildren() ) {
			a.setDefaultFromVisual();
		}
	}

	@Override
	public CompositeArg clone()
	{
		CompositeArg cl = (CompositeArg) super.clone();
		cl.argGroup = argGroup.clone();
		return cl;
	}


}
