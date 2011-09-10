package j.combot.validator;

import j.util.util.Asserts;

import java.util.Collections;
import java.util.List;

public final class LengthValidator extends Validator<String>
{
	private final int
		max,
		min;

	private final boolean useMax;

	public LengthValidator( int min, int max )
	{
		this.max = max;
		this.min = min;
		useMax = true;
		validate( true );
	}

	private void validate( boolean m ) {
		if ( m ) Asserts.arg( min <= max && useMax, "max can not be less than min" );
		Asserts.arg( min >= 0, "min can not be less than 0" );
	}

	public LengthValidator( int min )
	{
		this.min = min;
		this.max = Integer.MIN_VALUE;
		useMax = false;
		validate( false );
	}

	@Override
	protected List<ValEntry> validateInt( String value )
	{
		if ( value.length() < min ||
				( useMax && value.length() > max ) ) {
			// There is an error
			String m = "";

			if ( useMax || min != 1 ) {
				m = "The value must be " + min + " or more";
				// Add message about max limit
				if ( useMax ) m += " and " + max + " or less";
				m += " characters long";
			} else {
				m = "This value can not be emptry";
			}

			return Collections.singletonList( new ValEntry( m ) );

		} else {
			// Everythings ok
			return Collections.emptyList();
		}
	}
}