package j.combot.gui.visuals.specialized;

import static org.eclipse.swt.SWT.FILL;
import static org.eclipse.swt.SWT.LEFT;
import static org.eclipse.swt.SWT.NONE;
import j.combot.command.Arg;
import j.combot.gui.visuals.BaseArgVisual;
import j.combot.gui.visuals.VisualFactory;
import j.combot.util.SwtUtil;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


public class CommandVisual extends BaseArgVisual<Void> {

	@Override
	public void makeWidget( Arg<Void> cmd, Composite parent, Button b, VisualFactory visualFactory )
	{
		Composite page = new Composite( parent, NONE );
		page.setLayoutData( new GridData( FILL, LEFT, true, false, 2, 1 ) );
		GridLayout pageLayout = new GridLayout( 2, false );
		page.setLayout( pageLayout );

		Label title = new Label( page, NONE );
		title.setFont( SwtUtil.TITLE_FONT );
		title.setText( cmd.getTitle() );

		Label name = new Label( page, NONE );
		name.setFont( SwtUtil.BIG_FONT );
		name.setText( " (" + cmd.getName() + ")" );
	}

	@Override
	public Void getValue() {
		throw new UnsupportedOperationException();
	}

}
