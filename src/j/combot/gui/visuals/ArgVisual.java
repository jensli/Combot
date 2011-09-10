package j.combot.gui.visuals;

import j.combot.command.Arg;
import j.combot.validator.ValEntry;

import java.util.List;

public interface ArgVisual<T>
{
	public T getValue();

	public Arg<T> getArg();

	public void setArg( Arg<T> commandPart );

	public void setValidateResult( List<ValEntry> errors );

	public boolean isEnabled();

	public void setEnabled( boolean b );

	public void dispose();
}
