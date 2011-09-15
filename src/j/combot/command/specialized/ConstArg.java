package j.combot.command.specialized;

import j.combot.command.Arg;
import j.combot.gui.visuals.VisualTypes;

import java.util.Collections;
import java.util.List;

public class ConstArg extends Arg<Object>
{
	// Superclass clone method is sufficient

	public ConstArg( String title, String value )
	{
		super( title, "const name" );
		setVisualType( VisualTypes.CONST_TYPE );
		setDefaultValue( value );
	}

	@Override
	public List<String> getArgStrings() {
		return Collections.singletonList( getDefaultValue().toString() );
	}
}
