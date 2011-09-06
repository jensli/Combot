package j.combot.command;

import j.combot.gui.visuals.VisualTypes;
import j.util.prefs.PrefNode;

import java.util.Collections;
import java.util.List;



public class OptArg extends Arg<Boolean>
{
	// Super class clone method is sufficient
	@PrefNode
	private Arg<?> child;

	public OptArg( String title, String name, boolean def, Arg<?> child )
	{
		super( title, name );

		this.child = child;

		setDefaultValue( def );
		setVisualType( VisualTypes.OPT_TYPE );
		setName( "Opt arg: " + child.getName() );
	}

	public OptArg( String title, boolean def, Arg<?> child )
	{
		this( title, "", def, child );
	}

	@Override
	public void setDefaultFromVisual()
	{
		super.setDefaultValue( getVisual().getValue() );
		child.setDefaultFromVisual();
	}



	public Arg<?> getChild() {
		return child;
	}

	@Override
	public OptArg clone()
	{
		OptArg clone = (OptArg) super.clone();
		clone.child = child.clone();
		return clone;
	}

	@Override
	public List<String> getArgStrings()
	{
		if ( !getVisual().getValue() ) {
			return Collections.emptyList();
		} else {
			return child.getArgStrings();
		}
	}

}


//public class OptArg extends ComposedArg<Boolean>
//{
//	// Super class clone method is sufficient
//
//	public OptArg( String title, String name, boolean def, Arg<?>... childs )
//	{
//		super( title, name, childs );
//
//		setDefaultValue( def );
//		setVisualType( VisualTypes.OPT_TYPE );
//		setName( StringUtil.join( getArgGroup().getNames() , " " ) );
//	}
//
//	public OptArg( String title, boolean def, Arg<?>... childs )
//	{
//		this( title, "", def, childs );
//	}
//
//
//
//	@Override
//	public void setDefaultFromVisual() {
//		super.setDefaultFromVisual(); // ComposedArg
//		setDefaultValue( getVisual().getValue() ); // Reimplement Arg
//	}
//
//	@Override
//	public List<String> getArgStrings()
//	{
//		if ( !getVisual().getValue() ) {
//			return Collections.emptyList();
//		} else {
//			return getArgGroup().getArgStrings();
//		}
//	}
//
//}



