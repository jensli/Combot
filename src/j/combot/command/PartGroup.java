package j.combot.command;

public interface PartGroup<P extends CommandPart<?>> {
	public PartContainer<P> getPartContainer();
}
