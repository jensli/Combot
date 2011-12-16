package j.combot.validator;

import java.util.List;


public class InvValidator<T> extends Validator<T>
{
	private Validator<T> val;

	public InvValidator( Validator<T> val ) {
		this.val = val;
	}

	@Override
	protected List<ValEntry> validateInt( T value ) {
		return standardCreateList( !val.validate( value ).isEmpty(),
					value.toString() + " is not a valid value" );
	}
}
