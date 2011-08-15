package j.combot.gui.visuals;

import j.combot.command.CommandPart;
import j.combot.command.ValEntry;

import java.util.List;


//public interface PartVisual<T>
public interface PartVisual<T>
{
	public abstract T getValue();
	public CommandPart<T> getCommandPart();
	public void setCommandPart( CommandPart<T> commandPart );
	public void setValidateResult( List<ValEntry> errors );
}
