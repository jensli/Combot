package j.combot.command;

import j.combot.gui.visuals.VisualTypes;
import j.util.prefs.PrefNodeCollection;

import java.util.Collection;
import java.util.List;


public class ComposedArg extends Arg<Object>
{
	@PrefNodeCollection
	private ArgGroup argGroup = new ArgGroup();

	public ComposedArg( String title, Arg<?>... children )
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
	public ComposedArg clone()
	{
		ComposedArg cl = (ComposedArg) super.clone();
		cl.argGroup = argGroup.clone();
		return cl;
	}


}
