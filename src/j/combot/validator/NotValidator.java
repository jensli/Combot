package j.combot.validator;

import j.util.util.Util;

import java.util.List;


public class NotValidator extends Validator<Object>
{
	private Object notObject;

	public NotValidator( Object notObject ) {
		this.notObject = notObject;
	}

	@Override protected List<ValEntry> validateInt( Object value ) {
		return standardValidate( Util.equals( notObject, value ), "Value can not be " + value );
	}

}
