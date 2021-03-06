package j.combot.command;

import j.combot.gui.visuals.ArgVisual;
import j.combot.gui.visuals.VisualType;
import j.combot.validator.ValEntry;
import j.combot.validator.Validator;
import j.util.prefs.PrefName;
import j.util.prefs.PrefNodeName;
import j.util.prefs.PrefValue;
import j.util.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Arg<T> implements Cloneable
{
	// Pay attention to cloning when adding more fields

	// The value that is set to the visual when the arg is loaded.
	@PrefValue
	@PrefName( "def" )
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
		List<ValEntry> entires = new ArrayList<>();

		for ( ValEntry e : validator.validate( getVisual().getValue() ) ) {
		    entires.add( e.substSender( this ) );
		}

		return entires;
	}

	public List<String> getArgStrings()
	{
		String value = getVisual().getValue().toString();

		return  getName().isEmpty() ?
		        Collections.singletonList( value )
		        : Arrays.asList( getName(), value );
	}


	@SuppressWarnings( "unchecked" )
	@Override
	public Arg<T> clone() {
		try {
			return (Arg<T>) super.clone();
		} catch ( CloneNotSupportedException exc ) {
			throw new RuntimeException( exc );
		}
	}

	public void setDefaultFromVisual() {
        setDefaultValue( getVisual().getValue() );
    }

	/////////////////////////////////////////////////////////////

	public void setDefaultValue( T defaultValue ) {
		this.defaultValue = defaultValue;
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

	public int getPrio() {
		return prio;
	}

	public void setPrio( int prio ) {
		this.prio = prio;
	}
}
