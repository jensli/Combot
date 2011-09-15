package j.combot.command.specialized;

import j.combot.command.Arg;
import j.combot.gui.visuals.VisualTypes;
import j.combot.validator.Validator;

public class StringArg extends Arg<String>
{
	// Superclass clone method is sufficient

	public StringArg( String title, String name )
	{
		this( title, name, "" );
	}

	public StringArg( String title, String name, String defaultValue ) {
		this( title, name, defaultValue, Validator.EMPTY_VALIDATOR );
	}

	public StringArg( String title, String name, String defaultValue, Validator<? super String> validator ) {
		super( title, name, defaultValue, validator );
		setVisualType( VisualTypes.STRING_TYPE );
	}

}
