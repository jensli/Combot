package j.combot.command;

import j.combot.gui.visuals.OptVisual;
import j.combot.gui.visuals.VisualTypes;
import j.util.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class OptArg extends Arg<List<Object>> implements PartGroup<Arg<?>>
{
	private List<Arg<?>> childs;
	private PartContainer<Arg<?>> parts = new PartContainer<Arg<?>>();

	public OptArg( String title, String name, Arg<?>... childs )
	{
		super( title, name );
		this.childs = Arrays.asList( childs );
		parts.addAll( childs );

		setVisualType( VisualTypes.STD_OPT_TYPE );

		setName( StringUtil.join( parts.getNames() , " " ) );


//		List<String> l = new ArrayList<String>( childs.length );
//		for ( Arg<?> arg : childs ) {
//			l.add( arg.getName() );
//		}
//		setName( StringUtil.join( l, " " ) );
	}

	public OptArg( String title, Arg<?>... childs )
	{
		this( title, "", childs );
	}




	@Override public PartContainer<Arg<?>> getPartContainer() {
		return parts;
	}

	@Override
	public List<String> getArgStrings()
	{
		if ( !((OptVisual)getVisual()).isEnabled() ) {
			return Collections.emptyList();
		}

		List<String> list = new ArrayList<String>( childs.size() );
		for ( Arg<?> arg : childs ) {
			list.addAll( arg.getArgStrings() );
		}

		return list;
	}


	public List<Arg<?>> getChilds() {
		return childs;
	}
}
