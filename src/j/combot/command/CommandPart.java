package j.combot.command;

import j.combot.gui.visuals.PartVisual;

import org.eclipse.swt.widgets.Composite;

//public abstract class CommandPart<T>
public abstract class CommandPart<T, S extends CommandPart<T, S>>
{
	private String title;
	private String name;
	private PartVisual<T, S> visual;

	public CommandPart( String title, String name, PartVisual<T, S> visual )
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

	public PartVisual<T, S> getVisual() {
		return visual;
	}

	@SuppressWarnings( "unchecked" )
	public void makeWidget( Composite parent ) {
		visual.makeWidget( (S) this, parent );
	}
}
