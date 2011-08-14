package j.combot.command;

import j.combot.gui.visuals.PartVisual;

//public abstract class CommandPart<T>
public abstract class CommandPart<T>
{
	private String title;
	private String name;
	private PartVisual<T> visual;

	public CommandPart( String title, String name, PartVisual<T> visual )
	{
		this.title = title;
		this.name = name;
		this.visual = visual;
	}

	public String getTitle() {
		return title;
	}

	public String getName() {
		return name;
	}

	public PartVisual<T> getVisual() {
		return visual;
	}

	public static <T> Arg<T> cast( CommandPart<T> part ) {
		return (Arg<T>) part;
	}



}
