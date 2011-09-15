package j.combot.command;

import j.combot.gui.visuals.VisualTypes;
import j.combot.validator.ValEntry;

import java.util.Collections;
import java.util.List;

public class Sep extends Arg<Void> {

	public Sep() {
		super( "Sep", "Sep" );
		setVisualType( VisualTypes.SEP_TYPE );
	}

	@Override
	public List<ValEntry> validate() {
		return Collections.emptyList();
	}

	@Override
	public List<String> getArgStrings() {
		return Collections.emptyList();
	}


}
