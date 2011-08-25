package j.combot.command;

import java.util.Collections;
import java.util.List;

/**
 * There objects are shared, no individual arg data should be stored here.
 */

public abstract class Validator<T>
{
	public abstract List<ValEntry> validate( T value );

	protected static List<ValEntry> standardValidate( boolean ok, String message )
	{
		if ( ok ) {
			return Collections.emptyList();
		} else {
			return Collections.singletonList( new ValEntry( message ) );
		}
	}
}
