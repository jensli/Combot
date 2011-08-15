package j.combot.command;

import j.combot.gui.visuals.OptVisual;
import j.combot.gui.visuals.VisualTypes;
import j.util.util.StringUtil;

import java.util.Collections;
import java.util.List;


public class OptArg extends ComposedArg<Boolean>
{
	private ArgGroup parts = new ArgGroup();

	public OptArg( String title, String name, Arg<?>... childs )
	{
		super( title, name, childs );
		parts.addAll( childs );

		setVisualType( VisualTypes.STD_OPT_TYPE );

		setName( StringUtil.join( parts.getNames() , " " ) );
	}

	public OptArg( String title, Arg<?>... childs )
	{
		this( title, "", childs );
	}



	@Override
	public List<String> getArgStrings()
	{
		if ( !((OptVisual)getVisual()).isEnabled() ) {
			return Collections.emptyList();
		} else {
			return getArgGroup().getArgStrings();
		}
	}

}
