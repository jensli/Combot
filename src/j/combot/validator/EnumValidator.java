package j.combot.validator;

import j.util.util.StringUtil;

import java.util.List;

public class EnumValidator extends Validator<Object> {

	private List<? extends Object> legalValues;

	private boolean invert = false;

	public EnumValidator( List<? extends Object> legalValues, boolean invert ) {
		this.legalValues = legalValues;
		this.invert = invert;
	}

	public EnumValidator( List<? extends Object> legalValues ) {
		this( legalValues, false );
	}

	@Override
	protected List<ValEntry> validateInt( Object value )
	{
		String values = StringUtil.join( legalValues, ", " );

		if ( invert ) {
			return standardValidate(  !legalValues.contains( value ),
					"The value can not be one of: " + values  );
		} else {
			return standardValidate( legalValues.contains( value ),
					"The value must be one of: " + values  );
		}
	}

}
