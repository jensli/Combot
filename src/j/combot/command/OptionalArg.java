package j.combot.command;

import j.combot.gui.visuals.PartVisual;

public class OptionalArg extends Arg<String, OptionalArg>
{

	public OptionalArg( String title, String name,
			PartVisual<String, OptionalArg> visual, String defaultValue,
			Validator validator ) {
		super( title, name, defaultValue, validator, visual );
		// TODO Auto-generated constructor stub
	}

	public OptionalArg( String title, String name,
			PartVisual<String, OptionalArg> visual ) {
		super( title, name, visual );
		// TODO Auto-generated constructor stub
	}

}
