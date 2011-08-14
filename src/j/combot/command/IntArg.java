package j.combot.command;

import j.combot.gui.visuals.IntVisual;
import j.combot.gui.visuals.PartVisual;


public class IntArg extends Arg<Integer, IntArg>
{
	private int min, max;

	public IntArg( String title, String name, int min, int max, int defaultValue )
	{
		this( title, name, min, max, defaultValue, new IntVisual(), NULL_VALIDATOR );
	}

	public IntArg( String title, String name ) {
		this( title, name, Integer.MIN_VALUE, Integer.MAX_VALUE, 0 );
	}

	public IntArg( String title, String name, int min, int max,
			int defaultValue, PartVisual<Integer, IntArg> visual, Validator v )
	{
		super( title, name, defaultValue, v, visual );
		this.min = min;
		this.max = max;
	}


	public IntArg( String title, String name, PartVisual<Integer, IntArg> visual ) {
		super( title, name, visual );
		// TODO Auto-generated constructor stub
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

}
