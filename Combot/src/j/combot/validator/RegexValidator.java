package j.combot.validator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class RegexValidator extends Validator<String>
{
	private Pattern pattern;
	private String errorMessage;

	public RegexValidator( String pattern ) {
		this( pattern, null );
	}



	public RegexValidator( String pattern, String errorMessage )
	{
		this.pattern = Pattern.compile( pattern );
		this.errorMessage = errorMessage;
	}


	@Override
	protected List<ValEntry> validateInt( String value )
	{
		if ( pattern.matcher( value ).matches() ) {
			String msg = (errorMessage == null ? errorMessage :
				"Input does not matches regex: ") + pattern.pattern();
			return Arrays.asList( new ValEntry( msg ) );
		} else {
			return Collections.emptyList();
		}
	}

}
