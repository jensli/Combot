package j.combot.gui.visuals.specialized;

import static org.eclipse.swt.SWT.LEFT;
import static org.eclipse.swt.SWT.NONE;
import static org.eclipse.swt.SWT.TOP;
import j.combot.command.Arg;
import j.combot.command.specialized.CollapsableArg;
import j.combot.gui.visuals.BaseArgVisual;
import j.combot.gui.visuals.GuiArgVisual;
import j.combot.gui.visuals.VisualFactory;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;

public class CollapsableVisual extends BaseArgVisual<Void> {

	@Override
	public Void getValue() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void makeWidget( Arg<Void> arg, Composite parent,
			Button parentLabel, VisualFactory visualFactory ) {
		CollapsableArg optArg = (CollapsableArg) arg;
		Arg<?> childArg = optArg.getChild();

		// Button that enables/disables children
//		enabledBtn = new Button( parent, CHECK );
		ExpandBar expandBar = new ExpandBar( parent, NONE );
		expandBar.setLayoutData( new GridData( LEFT, TOP, false, false ) );
		ExpandItem item = new ExpandItem( expandBar, NONE );
		// Child will set the text to its title, its not done here

//		enabledBtn.setSelection( optArg.getDefaultValue() );

//		enabledBtn.setLayoutData( layoutData );

//		enabledBtn.addSelectionListener( new SelectionAdapter() {
//			@Override public void widgetSelected( SelectionEvent e ) {
//				setEnabled( enabledBtn.getSelection() );
//			}
//		} );

		GuiArgVisual<?> childVisual = visualFactory.make( childArg );
		childVisual.makeWidget( (Arg) childArg, parent, visualFactory );
	}






}
