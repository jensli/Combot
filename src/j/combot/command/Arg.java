package j.combot.command;

import j.combot.gui.visuals.ArgVisual;
import j.combot.gui.visuals.VisualType;
import j.util.util.Util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Arg<T> implements Cloneable
{
	// Pay attention to cloning when adding more fields

	public static final Validator<String> EMPTY_VALIDATOR = new LengthValidator( 1 );

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

	public List<ValEntry> validate()
	{
		List<ValEntry> entires = validator.validate( getVisual().getValue() );
		for ( ValEntry e : entires ) {
			e.sender = this;
		}
		return entires;
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



	public void setDefaultValue( T defaultValue ) {
		this.defaultValue = defaultValue;
	}

	@Override
	public String toString() {
		return Util.simpleToString( this, getName() );
	}

	public T getDefaultValue() {
		return defaultValue;
	}

	@SuppressWarnings( "unchecked" )
	public Arg<T> clone() {
		try {
			Arg<T> res;
			res = (Arg<T>) super.clone();
			res.visual = null;
			return res;
		} catch ( CloneNotSupportedException exc ) {
			throw new RuntimeException( exc );
		}
	}

//	public Arg<T> copy() {
//		return new Arg<>( title, name, defaultValue, validator );
//	}

}
