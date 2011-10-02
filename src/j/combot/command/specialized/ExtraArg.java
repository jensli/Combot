package j.combot.command.specialized;

import j.combot.command.Arg;
import j.combot.gui.visuals.VisualTypes;
import j.combot.validator.Validator;
import j.util.util.StringUtil;

import java.util.List;

public class ExtraArg extends Arg<String>
{
	public ExtraArg( String title )
	{
		super( title, "Extra args " + title, "", Validator.NULL_VALIDATOR );
		setVisualType( VisualTypes.STRING_TYPE );
	}

	@Override
	public List<String> getArgStrings()
	{
		return StringUtil.splitWithQuotes( getVisual().getValue() );
	}
}
