package j.combot.command.specialized;

import j.combot.command.Arg;
import j.combot.command.DelArg;
import j.combot.gui.visuals.VisualTypes;
import j.combot.validator.ValEntry;

import java.util.Collections;
import java.util.List;


/**
 * Makes its child optional, the user can choose if its child will be included
 * or not
 */
public class OptArg extends DelArg<Boolean>
{
	public OptArg( boolean def, Arg<?> child )
	{
		super( "OptArg", "OptArg", child );

		setDefaultValue( def );
		setVisualType( VisualTypes.OPT_TYPE );
		setName( "Opt arg: " + child.getName() );
	}

	@Override
	public List<String> getArgStrings()
	{
		if ( getVisual().getValue() ) {
			return super.getArgStrings();
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public List<ValEntry> validate()
	{
		if ( getVisual().getValue() ) {
			return super.validate();
		} else {
			return Collections.emptyList();
		}
	}

}


