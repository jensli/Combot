package j.combot.gui.visuals;

import static org.eclipse.swt.SWT.NONE;
import j.combot.command.Arg;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


public class SepVisual extends BaseArgVisual<Void> {

	@Override
	public Void getValue() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void makeWidget( Arg<Void> arg, Composite parent,
			Button parentLabel, VisualFactory visualFactory ) {
		new Label( parent, NONE );
		new Label( parent, NONE );
	}




}
