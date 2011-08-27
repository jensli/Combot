package j.combot.gui.visuals;

import static org.eclipse.swt.SWT.CHECK;
import static org.eclipse.swt.SWT.FILL;
import static org.eclipse.swt.SWT.LEFT;
import static org.eclipse.swt.SWT.NONE;
import j.combot.command.Arg;
import j.combot.command.OptArg;
import j.combot.gui.GuiGlobals;
import j.combot.gui.misc.GuiUtil;
import j.swt.util.SwtUtil;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class OptVisual extends BaseArgVisual<Boolean>
{
	private Button enabled;

	@SuppressWarnings( "unused" )
	private boolean isEnabled = false;  // Save state here is Control is incorrect?

	private Composite comp;

	@Override
	public Boolean getValue() {
		return isEnabled();
	}



	@Override
	public void makeWidget(
			Arg<Boolean> part, Composite parent, VisualFactory visualFactory )
	{
		OptArg optArg = (OptArg) part;

		enabled = new Button( parent, CHECK );
		enabled.setText( optArg.getTitle() );
		enabled.setSelection( part.getDefaultValue() );
		GridData butData = new GridData();
		butData.horizontalSpan = 2;
		enabled.setLayoutData( butData );
		setValueControl( enabled );


		if ( !optArg.getArgGroup().isEmpty() ) {
			comp = new Composite( parent, NONE );
			comp.setLayout( new GridLayout( 2, false ) );
			GridData compData = new GridData( FILL, LEFT, true, false );
			compData.horizontalSpan = 2;
			compData.horizontalIndent = (int) ( GuiGlobals.UNIT * 1.5 );
			comp.setLayoutData( compData );

			enabled.addSelectionListener( new SelectionAdapter() {
				@Override public void widgetSelected( SelectionEvent e ) {
					SwtUtil.recursiveSetEnabled( comp, isEnabled() );
				}
			});

			for ( Arg<?> arg : optArg.getArgGroup() ) {
				GuiUtil.createVisual( arg, comp, visualFactory );
			}

			SwtUtil.recursiveSetEnabled( comp, isEnabled() );
		}
	}

	public boolean isEnabled() {
		return enabled.getSelection();
	}



	@Override
	public void dispose() {

	}




}
