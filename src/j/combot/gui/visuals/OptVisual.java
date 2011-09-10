package j.combot.gui.visuals;

import static org.eclipse.swt.SWT.CHECK;
import static org.eclipse.swt.SWT.FILL;
import static org.eclipse.swt.SWT.LEFT;
import static org.eclipse.swt.SWT.NONE;
import static org.eclipse.swt.SWT.TOP;
import j.combot.command.Arg;
import j.combot.command.OptArg;
import j.swt.util.SwtUtil;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;





public class OptVisual extends BaseArgVisual<Boolean>
{
	private Button enabledBtn;

	@Override
	public Boolean getValue() {
		return isEnabled();
	}



	@Override
	public void makeWidget(
			Arg<Boolean> arg, Composite parent, Composite childrenParent, VisualFactory visualFactory )
	{
		OptArg optArg = (OptArg) arg;


		Composite checkComp = new Composite( parent, NONE );
		checkComp.setLayout( new GridLayout( 2, false ) );
		checkComp.setLayoutData( new GridData( FILL, FILL, true, false, 2, 1 ) );

		// Button that enables/disables children
		enabledBtn = new Button( checkComp, CHECK );
		enabledBtn.setText( "" );
		enabledBtn.setSelection( optArg.getDefaultValue() );
		enabledBtn.setLayoutData( new GridData( LEFT, TOP, false, false, 1, 1 ) );
//		GridData btnData = new GridData();
//		enabledBtn.setLayoutData( btnData );
		setValueControl( enabledBtn );

		final Composite firstComp = new Composite( checkComp, NONE );
		firstComp.setLayoutData( new GridData( FILL, FILL, true, false, 1, 1 ) );
		firstComp.setLayout( new GridLayout( 2, false ) );

//		firstComp.setBackground( SwtStdValues.BLUE );


//		// Add panel for children
//		final Composite childsComp = new Composite( parent, NONE );
//		childsComp.setLayout( new GridLayout( 2, false ) );
//		GridData compData = new GridData( FILL, FILL, true, false );
//		compData.horizontalSpan = 2;
//		compData.horizontalIndent = (int) ( SwtStdValues.UNIT * 1.5 );
//		childsComp.setLayoutData( compData );

		enabledBtn.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected( SelectionEvent e ) {
//				SwtUtil.recursiveSetEnabled( childsComp, isEnabled() );
				SwtUtil.recursiveSetEnabled( firstComp, isEnabled() );

				firstComp.setEnabled( true );
				enabledBtn.setEnabled( true );
			}
		} );

		Arg<?> arg1 = optArg.getChild();

		visualFactory.make( arg1 ).makeWidget( (Arg) arg1, firstComp, null, visualFactory );
	}

	public boolean isEnabled() {
		return enabledBtn.getSelection();
	}



}

//public class OptVisual extends BaseArgVisual<Boolean>
//{
//	private Button enabledBtn;
//
//	@Override
//	public Boolean getValue() {
//		return isEnabled();
//	}
//
//
//
//	@Override
//	public void makeWidget(
//			Arg<Boolean> part, Composite parent, VisualFactory visualFactory )
//	{
//		OptArg optArg = (OptArg) part;
//
//		// Button that enables/disables children
//		enabledBtn = new Button( parent, CHECK );
//		enabledBtn.setText( optArg.getTitle() );
//		enabledBtn.setSelection( part.getDefaultValue() );
//		GridData butData = new GridData();
//		butData.horizontalSpan = 2;
//		enabledBtn.setLayoutData( butData );
//		setValueControl( enabledBtn );
//
//		// Add panel for children
//		final Composite childsComp = new Composite( parent, NONE );
//		childsComp.setLayout( new GridLayout( 2, false ) );
//		GridData compData = new GridData( FILL, LEFT, true, false );
//		compData.horizontalSpan = 2;
//		compData.horizontalIndent = (int) ( SwtStdValues.UNIT * 1.5 );
//		childsComp.setLayoutData( compData );
//
//		enabledBtn.addSelectionListener( new SelectionAdapter() {
//			@Override public void widgetSelected( SelectionEvent e ) {
//				// TODO: This enables/disables ALL children, should only be
//				// parent. When switching back, children should regain their
//				// previous state, not all of them should be enabled.
//				SwtUtil.recursiveSetEnabled( childsComp, isEnabled() );
//			}
//		} );
//
//		// Loop over children and add them reursivly.
//		for ( Arg<?> arg : optArg.getArgGroup() ) {
//			GuiUtil.createVisual( arg, childsComp, visualFactory );
//		}
//
//		SwtUtil.recursiveSetEnabled( childsComp, isEnabled() );
//	}
//
//	public boolean isEnabled() {
//		return enabledBtn.getSelection();
//	}
//
//
//
//}
