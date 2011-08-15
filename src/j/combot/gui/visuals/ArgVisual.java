package j.combot.gui.visuals;

import j.combot.command.Arg;
import j.combot.command.ValEntry;

import java.util.List;


//public interface PartVisual<T>
public interface ArgVisual<T>
{
	public abstract T getValue();
	public Arg<T> getCommandPart();
	public void setCommandPart( Arg<T> commandPart );
	public void setValidateResult( List<ValEntry> errors );
}
