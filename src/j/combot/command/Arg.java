package j.combot.command;

import j.combot.gui.visuals.PartVisual;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Arg<T> extends CommandPart<T>
{
	private T defaultValue;
	private Validator validator;

	public final static Validator NULL_VALIDATOR = new Validator() {
		@Override public List<String> validate( String value ) {
			return Collections.emptyList();
		}
	};

	public Arg( String title, String name, PartVisual<T> visual )
	{
		this( title, name, null, NULL_VALIDATOR, visual );
	}



	public Arg( String title, String name, T defaultValue,
			Validator validator, PartVisual<T> visual ) {
		super( title, name, visual );
		this.defaultValue = defaultValue;
	}



	public boolean hasArgString() {
		return !getVisual().getValue().toString().isEmpty();
	}


	public List<String> validate( String text ) {
		return validator.validate( text );
	}

	public List<String> getArgString()
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

	@Override public String toString() {
		return "<" + getName() + ">";
	}



	public T getDefaultValue() {
		return defaultValue;
	}



}
