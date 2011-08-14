package j.combot.command;

import j.combot.gui.visuals.PartVisual;
import j.combot.gui.visuals.StringVisual;

public class StringArg extends Arg<String, StringArg>
{
	public StringArg( String title, String name )
	{
		this( title, name, "" );
	}

	public StringArg( String title, String name, String defaultValue ) {
		this( title, name, defaultValue, new StringVisual() );
	}

	public StringArg( String title, String name, String defaultValue, Validator val ) {
		this( title, name, defaultValue, new StringVisual() );
	}

	public StringArg( String title, String name, String defaultValue,
			PartVisual<String, StringArg> visual ) {
		this( title, name, visual, defaultValue, NULL_VALIDATOR );
	}

	public StringArg( String title, String name,
			PartVisual<String, StringArg> visual, String defaultValue,
			Validator validator ) {
		super( title, name, defaultValue, validator, visual );
	}

}
