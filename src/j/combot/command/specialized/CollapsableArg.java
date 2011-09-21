package j.combot.command.specialized;

import j.combot.command.Arg;
import j.combot.command.DelArg;
import j.combot.gui.visuals.VisualTypes;

public class CollapsableArg extends DelArg<Void> {

	public CollapsableArg( String title, String name, Arg<?> child ) {
		super( title, name, child );
		setVisualType( VisualTypes.COLLAPSABLE_TYPE );
	}

}
