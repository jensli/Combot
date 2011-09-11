package j.combot.command;

import j.combot.gui.visuals.VisualTypes;
import j.combot.validator.ValEntry;

import java.util.Collections;
import java.util.List;



public class OptArg extends DelArg<Boolean>
{
	public OptArg( boolean def, Arg<?> child )
	{
		super( "OptArg", "OptArg", child );

		setDefaultValue( def );
		setVisualType( VisualTypes.OPT_TYPE );
		setName( "Opt arg: " + child.getName() );
	}

	@Override
	public List<String> getArgStrings()
	{
		if ( getVisual().getValue() ) {
			return super.getArgStrings();
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public List<ValEntry> validate() {
		if ( !getVisual().getValue() ) {
			return Collections.emptyList();
		} else {
			return super.validate();
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



