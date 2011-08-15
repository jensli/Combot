package j.combot.command;

import j.util.util.Util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Arg<T> extends CommandPart<T>
{
	private T defaultValue;
	private Validator<? super T> validator;

	public static final Validator<Object> NULL_VALIDATOR = new Validator<Object>() {
		@Override public List<ValEntry> validate( Object value ) {
			return Collections.emptyList();
		}
	};

	public static final Validator<String> EMPTY_VALIDATOR = new Validator<String>() {
		@Override public List<ValEntry> validate( String value ) {
			if ( value.isEmpty() ) {
				return Collections.singletonList( new ValEntry( "This field can not be empty" ) );
			} else {
				return Collections.emptyList();
			}
		}
	};


	public Arg( String title, String name  )
	{
		this( title, name, null, NULL_VALIDATOR );
	}

	public Arg( String title, String name, T defaultValue, Validator<? super T> validator )
	{
		super( title, name );
		this.defaultValue = defaultValue;
		this.validator = validator;
	}


	public List<ValEntry> validate() {
		return validator.validate( getVisual().getValue() );
	}

	public List<String> getArgStrings()
	{
		String value = getVisual().getValue().toString();

		if ( value.isEmpty() ) {
			return Collections.emptyList();
		}

		List<String> l = new LinkedList<String>();

		if ( !getName().isEmpty() ) {
			l.add( getName() );
		}

		l.add( value );

		return l;
	}

	@Override
	public String toString() {
		return Util.simpleToString( this, getName() );
	}

	public T getDefaultValue() {
		return defaultValue;
	}



}
