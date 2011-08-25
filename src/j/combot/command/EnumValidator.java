package j.combot.command;

import java.util.Collections;
import java.util.List;

public class EnumValidator extends Validator<Object> {

	private List<? extends Object> legalValues;

	public EnumValidator( List<? extends Object> legalValues ) {
		this.legalValues = legalValues;
	}

	@Override
	public List<ValEntry> validate( Object value )
	{
		if ( legalValues.contains( value ) ) {
			return Collections.emptyList();
		} else {
			return Collections.singletonList(
					new ValEntry( value.toString() + " is not a valid value" ) );
		}
	}

}
