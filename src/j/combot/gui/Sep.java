package j.combot.gui;

import static org.eclipse.swt.SWT.SEPARATOR;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


public class Sep extends Label {

	public Sep( Composite parent, int style ) {
		super( parent, style | SEPARATOR );

	}

	@Override protected void checkSubclass() {}

}