package j.combot.command;

import j.combot.gui.visuals.ArgVisual;
import j.combot.gui.visuals.VisualType;
import j.util.util.Util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Arg<T>
{
	private T defaultValue;
	private Validator<? super T> validator;

	private String
		title,
		name;

	private ArgVisual<T> visual;
	private VisualType<T> visualType;

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


	public String getTitle() {
		return title;
	}

	public String getName() {
		return name;
	}

	public ArgVisual<T> getVisual() {
		return visual;
	}


	public void setVisual( ArgVisual<T> visual ) {
		this.visual = visual;
	}

	public VisualType<T> getVisualType() {
		return visualType;
	}

	public void setVisualType( VisualType<T> visualType ) {
		this.visualType = visualType;
	}

	public void setTitle( String title ) {
		this.title = title;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public Arg( String title, String name  )
	{
		this( title, name, null, NULL_VALIDATOR );
	}

	public Arg( String title, String name, T defaultValue, Validator<? super T> validator )
	{
		this.title = title;
		this.name = name;
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
		} else if ( getName().isEmpty() ) {
			return Arrays.asList( value );
		} else {
			return Arrays.asList( getName(), value );
		}
	}

	@Override
	public String toString() {
		return Util.simpleToString( this, getName() );
	}

	public T getDefaultValue() {
		return defaultValue;
	}
}
