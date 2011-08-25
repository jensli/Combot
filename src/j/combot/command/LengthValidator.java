package j.combot.command;

import j.util.util.Asserts;

import java.util.Collections;
import java.util.List;

public final class LengthValidator extends Validator<String>
{
	private int
		max = Integer.MIN_VALUE,
		min;

	private boolean useMax = true;

	public LengthValidator( int min, int max )
	{
		Asserts.arg( min <= max && useMax, "max can not be less than min" );
		Asserts.arg( min >= 0, "min can not be less than 0" );
		this.max = max;
		this.min = min;
	}

	public LengthValidator( int min ) {
		this.min = min;
		Asserts.arg( min >= 0, "min can not be less than 0" );
		useMax = false;
	}

	@Override
	public List<ValEntry> validate( String value ) {
		if ( value.length() < min ||
				( useMax && value.length() > max ) ) {

			String m = "";
			if ( min == 1 && !useMax ) {
				m = "The value can not be emptry";
			} else {

				m = "The value must be " + min + " or more";
				if ( useMax ) m = m + " and " + max + " or less ";
				m = m + " characters long";
			}
			return Collections.singletonList( new ValEntry( m ) );
		} else {
			return Collections.emptyList();
		}
	}
}