package j.combot.command;

import j.combot.gui.visuals.VisualTypes;

import java.util.Collections;
import java.util.List;

public class ConstArg extends Arg<String>
{
	private String value;

	public ConstArg( String title, String name, String value )
	{
		super( title, name );
		this.value = value;
		setVisualType( VisualTypes.STD_NULL_TYPE );
	}

	@Override
	public List<String> getArgStrings() {
		return Collections.singletonList( value );
	}


}
