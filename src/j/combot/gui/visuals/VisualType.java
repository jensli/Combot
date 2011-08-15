package j.combot.gui.visuals;

import j.util.util.Util;


public class VisualType<T> {
	private String name;

	public VisualType( String name ) {
		this.name = name;
	}

	@Override public String toString() {
		return Util.simpleToString( this, name );
	}


}