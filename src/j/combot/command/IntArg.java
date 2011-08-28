package j.combot.command;

import j.combot.gui.visuals.ArgVisual;
import j.combot.gui.visuals.VisualTypes;


public class IntArg extends Arg<Integer>
{
	// Super class clone method is sufficient

	private int min, max;

	{
		setVisualType( VisualTypes.INT_TYPE );
	}

	public IntArg( String title, String name, int min, int max, int defaultValue )
	{
		this( title, name, min, max, defaultValue, Validator.NULL_VALIDATOR );
	}

	public IntArg( String title, String name ) {
		this( title, name, Integer.MIN_VALUE, Integer.MAX_VALUE, 0 );
	}

	public IntArg( String title, String name, int min, int max,
			int defaultValue, Validator<? super Integer> v )
	{
		super( title, name, defaultValue, v );
		this.min = min;
		this.max = max;
	}


	public IntArg( String title, String name, ArgVisual<Integer> visual ) {
		super( title, name );
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

}
