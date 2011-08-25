package j.combot.command;

import java.util.List;

public class InvValidator<T> extends Validator<T>
{
	private Validator<T> val;

	public InvValidator( Validator<T> val ) {
		this.val = val;
	}

	@Override
	public List<ValEntry> validate( T value ) {
	return standardValidate( !val.validate( value ).isEmpty(),
				value.toString() + " is not a valid value" );
	}
}
