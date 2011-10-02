package j.combot.validator;

import j.util.util.StringUtil;

import java.util.List;

/**
 * Validator that checks if a string can be split with
 * StringUtil.splitWithQuotes()
 */
public class SplitStringValidator extends Validator<String> {

	@Override
	protected List<ValEntry> validateInt( String value )
	{
		String msg = null;

		try {
			StringUtil.splitWithQuotes( value );
		} catch ( IllegalArgumentException exc ) {
			msg = exc.getMessage();
		}

		return standardCreateList( msg == null, "Value is not well formed: " + msg  );
	}

}
