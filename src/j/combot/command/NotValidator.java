package j.combot.command;

import j.util.util.Util;

import java.util.List;

public class NotValidator extends Validator<Object>
{
	private Object notObject;

	public NotValidator( Object notObject ) {
		this.notObject = notObject;
	}

	@Override public List<ValEntry> validate( Object value ) {
		return standardValidate( Util.equals( notObject, value ), "Value can not be " + value );
	}

}
