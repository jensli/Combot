package j.combot.validator;

import java.util.Collections;
import java.util.List;

/**
 * There objects are shared, no individual arg data should be stored here.
 */

public abstract class Validator<T>
{
	private String overrideMessage = null;
	public static final Validator<String> EMPTY_VALIDATOR = new LengthValidator( 1 );
	public static final Validator<Object> NULL_VALIDATOR = new Validator<Object>() {
		@Override protected List<ValEntry> validateInt( Object value ) {
			return Collections.emptyList();
		}
	};

	public Validator() {}

	/**
	 * If overrideMessage is other then null, this message are used instead of
	 * Validator specific message.
	 */
	public Validator( String overrideMessage ) {
		this.overrideMessage = overrideMessage;
	}


	public final List<ValEntry> validate( T value )
	{
		List<ValEntry> result = validateInt( value );

		// Overwrite valitators message if overrideMessage is set
		if ( overrideMessage != null ) {
			for ( ValEntry e : result ) {
				e.message = overrideMessage;
			}
		}

		return result;
	}

	protected abstract List<ValEntry> validateInt( T value );

	protected static List<ValEntry> standardValidate( boolean ok, String message )
	{
		if ( ok ) {
			return Collections.emptyList();
		} else {
			return Collections.singletonList( new ValEntry( message ) );
		}
	}
}
