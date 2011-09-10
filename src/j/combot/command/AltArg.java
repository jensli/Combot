package j.combot.command;


public class AltArg extends Arg<Integer> {



	public AltArg( String title, Arg<?>... args ) {

		super( title, title );

		setDefaultValue( 0 );
//		setVisualType( VisualTypes.RADIO_TYPE );
	}
}
