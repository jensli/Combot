package j.combot.validator;

import j.util.util.IssueType;

import java.util.Collections;
import java.util.List;

/**
 * These objects are shared, no individual arg data should be stored here.
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


	/**
	 * Standart implementation to help with construction of result list.
	 * Calls template method validateInt.
	 */
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

	protected static List<ValEntry> standardCreateList( boolean ok, String message ) {
		return standardCreateList( ok, message, IssueType.ERROR );
	}

	protected static List<ValEntry> standardCreateList( boolean ok, String message, IssueType type )
	{
		if ( ok ) {
			return Collections.emptyList();
		} else {
			return Collections.singletonList( new ValEntry( message, type ) );
		}
	}
}
