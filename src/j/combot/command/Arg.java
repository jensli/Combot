package j.combot.command;

import j.combot.gui.visuals.ArgVisual;
import j.combot.gui.visuals.VisualType;
import j.combot.validator.ValEntry;
import j.combot.validator.Validator;
import j.util.prefs.PrefNodeName;
import j.util.prefs.PrefValue;
import j.util.util.Util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Arg<T> implements Cloneable
{
	// Pay attention to cloning when adding more fields

	// The value that is set to the visual when the arg is loaded.
	@PrefValue
	private T defaultValue;

	// The priority is used when a argument and its visualisation should not
	// be in the same order. Args are sorted acccordning to this value when
	// their value is taken. Arg visuals are sorted in the order they are added
	// in.
	private int prio = 0;

	// The text used to present the Arg in the gui.
	@PrefNodeName
	private String title;

	// Used as the acctual arg name, often in the form "-name"
	private String name;

	// prefix is prepended to arg when it is created, sufix if appended.
	private String
		valuePrefix, valueSufix;

	// Decides whethere the value in the visual is valid, or returns a list of
	// errors. Often run when value in visual changes.
	private Validator<? super T> validator;

	// The visual repersentation of the arg, used to get the value.
	// Set by the gui using the visualType.
	private ArgVisual<T> visual;

	// Read by the gui to decide which kind of visual to create for the arg.
	private VisualType<T> visualType;


	public Arg( String title, String name  )
	{
		this( title, name, null, Validator.NULL_VALIDATOR );
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
		for ( ValEntry e : entires ) e.sender = this;
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
			// Here is coded the behaviour to return args and parameters as
			// distingt tokens
			return Arrays.asList( getName(), value );
		}
	}



	@SuppressWarnings( "unchecked" )
	@Override
	public Arg<T> clone() {
		try {
			return (Arg<T>) super.clone();
		} catch ( CloneNotSupportedException exc ) {
			exc.printStackTrace();
			throw new RuntimeException( exc );
		}
	}


	/////////////////////////////////////////////////////////////

	public void setDefaultValue( T defaultValue ) {
		this.defaultValue = defaultValue;
	}

	public void setDefaultFromVisual() {
		setDefaultValue( getVisual().getValue() );
	}

	@Override
	public String toString() {
		return Util.simpleToString( this, getTitle(), getDefaultValue() );
	}

	public T getDefaultValue() {
		return defaultValue;
	}

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

	public String getValuePrefix() {
		return valuePrefix;
	}

	public void setValuePrefix( String valuePrefix ) {
		this.valuePrefix = valuePrefix;
	}

	public String getValueSufix() {
		return valueSufix;
	}

	public void setValueSufix( String valueSufix ) {
		this.valueSufix = valueSufix;
	}

	public int getPrio() {
		return prio;
	}

	public void setPrio( int prio ) {
		this.prio = prio;
	}
}
