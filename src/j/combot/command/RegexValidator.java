package j.combot.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class RegexValidator implements Validator
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
	public List<String> validate( String value )
	{
		if ( pattern.matcher( value ).matches() ) {
			String msg = (errorMessage == null ? errorMessage :
				"Input does not matches regex: ") + pattern.pattern();
			return Arrays.asList( msg );
		} else {
			return Collections.emptyList();
		}
	}

}
