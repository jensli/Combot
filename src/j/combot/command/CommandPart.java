package j.combot.command;

import j.combot.gui.visuals.PartVisual;
import j.combot.gui.visuals.VisualType;

import java.util.List;

//public abstract class CommandPart<T>
public abstract class CommandPart<T>
{
	private String
		title,
		name;

	private PartVisual<T> visual;
	private VisualType<T> visualType;

	public CommandPart( String title, String name )
	{
		this.title = title;
		this.name = name;
	}

	public abstract List<ValEntry> validate();
	public abstract List<String> getArgStrings();


	public String getTitle() {
		return title;
	}

	public String getName() {
		return name;
	}

	public PartVisual<T> getVisual() {
		return visual;
	}


	public void setVisual( PartVisual<T> visual ) {
		this.visual = visual;
	}

	public VisualType<T> getVisualType() {
		return visualType;
	}

	public void setVisualType( VisualType<T> visualType ) {
		this.visualType = visualType;
	}

	public void setTitle( String title ) {
		this.title = title;
	}

	public void setName( String name ) {
		this.name = name;
	}


}
