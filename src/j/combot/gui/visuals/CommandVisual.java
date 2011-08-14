package j.combot.gui.visuals;

import j.combot.command.Command;
import j.combot.gui.GuiGlobals;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


public class CommandVisual extends BasePartVisual<String, Command> {

	public void makeWidget( Command cmd, Composite parent )
	{
		Composite page = new Composite( parent, SWT.NONE );
		page.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		GridLayout pageLayout = new GridLayout( 1, false );
		page.setLayout( pageLayout );

		Label title = new Label( page, SWT.NONE );

		title.setFont( GuiGlobals.getTitleFont() );
		title.setText( cmd.getTitle() + " (" + cmd.getName() + ")" );
	}

	@Override public String getValue() {
		throw new UnsupportedOperationException();
	}

}
