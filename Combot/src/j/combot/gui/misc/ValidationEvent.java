package j.combot.gui.misc;

import j.combot.command.Arg;
import j.combot.validator.ValEntry;

import java.util.List;

public class ValidationEvent
{
	public List<ValEntry> entries;
	public Arg<?> sender;


	public ValidationEvent( List<ValEntry> entries, Arg<?> sender ) {
		this.entries = entries;
		this.sender = sender;
	}


	public ValidationEvent( List<ValEntry> entries )
	{
		this.entries = entries;
	}

}
