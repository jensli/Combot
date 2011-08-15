package j.combot.command;

import java.util.List;

public interface Validator<T> {
	public List<ValEntry> validate( T value );
}
