package j.combot.command;

import j.combot.gui.visuals.VisualTypes;

public class AltArg extends ComposedArg<Integer> {

	public AltArg( String title, String name, Arg<?>... args ) {
		super( title, name, args );

		setDefaultValue( 0 );
		setVisualType( VisualTypes.RADIO_TYPE );
	}
}
