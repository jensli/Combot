package j.combot.gui.visuals.specialized;

import static org.eclipse.swt.SWT.CHECK;
import static org.eclipse.swt.SWT.LEFT;
import static org.eclipse.swt.SWT.TOP;
import j.combot.command.Arg;
import j.combot.command.specialized.OptArg;
import j.combot.gui.visuals.BaseArgVisual;
import j.combot.gui.visuals.GuiArgVisual;
import j.combot.gui.visuals.VisualFactory;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;





public class OptVisual extends BaseArgVisual<Boolean>
{
	private Button enabledBtn;

	@SuppressWarnings( { "unchecked", "rawtypes" } )
	@Override
	public void makeWidget(
			Arg<Boolean> arg, Composite parent, Button parentLbl, VisualFactory visualFactory )
	{
		OptArg optArg = (OptArg) arg;
		Arg<?> childArg = optArg.getChild();

		// Button that enables/disables children
		enabledBtn = new Button( parent, CHECK );

		// Child will set the text to its title, its not done here

		enabledBtn.setSelection( optArg.getDefaultValue() );

		GridData layoutData = new GridData( LEFT, TOP, false, false, 1, 1 );

		enabledBtn.setLayoutData( layoutData );

		enabledBtn.addSelectionListener( new SelectionAdapter() {
			@Override public void widgetSelected( SelectionEvent e ) {
				setEnabled( enabledBtn.getSelection() );
			}
		} );

		GuiArgVisual<?> childVisual = visualFactory.make( childArg );
		childVisual.makeWidget( (Arg) childArg, parent, enabledBtn,  visualFactory );
	}

	@Override
	public void setEnabled( boolean b ) {
		super.setEnabled( b );
		( (OptArg) getArg() ).getChild().getVisual().setEnabled( b );
	}


	@Override
	public Boolean getValue() {
		return enabledBtn.getSelection();
	}



}

