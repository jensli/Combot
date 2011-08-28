package j.combot.gui.visuals;

import static org.eclipse.swt.SWT.FILL;
import static org.eclipse.swt.SWT.LEFT;
import static org.eclipse.swt.SWT.NONE;
import j.combot.command.Arg;
import j.swt.util.SwtStdValues;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


public class CommandVisual extends BaseArgVisual<String> {

	@Override
	public void makeWidget( Arg<String> cmd, Composite parent, VisualFactory visualFactory )
	{
		Composite page = new Composite( parent, NONE );
		page.setLayoutData( new GridData( FILL, LEFT, true, false, 2, 1 ) );
		GridLayout pageLayout = new GridLayout( 2, false );
		page.setLayout( pageLayout );

		Label title = new Label( page, NONE );
		title.setFont( SwtStdValues.TITLE_FONT );
		title.setText( cmd.getTitle() );

		Label name = new Label( page, NONE );
		name.setFont( SwtStdValues.BIG_FONT );
		name.setText( " (" + cmd.getName() + ")" );
	}

	@Override public String getValue() {
		throw new UnsupportedOperationException();
	}

}
