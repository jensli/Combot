package j.combot.command.specialized;

import j.combot.command.Arg;
import j.combot.gui.visuals.VisualTypes;
import j.combot.validator.SplitStringValidator;
import j.util.util.StringUtil;

import java.util.List;

public class ExtraArg extends Arg<String>
{
	public ExtraArg( String title )
	{
		super( title, "Extra args " + title, "", new SplitStringValidator() );
		setVisualType( VisualTypes.STRING_TYPE );
	}

	@Override
	public List<String> getArgStrings()
	{
		return StringUtil.splitWithQuotes( getVisual().getValue() );
	}
}
