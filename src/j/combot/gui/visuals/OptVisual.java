package j.combot.gui.visuals;

import static org.eclipse.swt.SWT.CHECK;
import static org.eclipse.swt.SWT.LEFT;
import static org.eclipse.swt.SWT.TOP;
import j.combot.command.Arg;
import j.combot.command.OptArg;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;





public class OptVisual extends BaseArgVisual<Boolean>
{
	private Button enabledBtn;

	@Override
	public Boolean getValue() {
		return isEnabled();
	}



	@SuppressWarnings( { "unchecked", "rawtypes" } )
	@Override
	public void makeWidget(
			Arg<Boolean> arg, Composite parent, Button parentLbl, VisualFactory visualFactory )
	{
		OptArg optArg = (OptArg) arg;


//		Composite checkComp = new Composite( parent, NONE );
//		checkComp.setLayoutData( new GridData( FILL, FILL, true, false, 2, 1 ) );
//		checkComp.setLayout( new GridLayout( 2, false ) );
//
//		SwtStdValues.setDebugColor( checkComp, SwtStdValues.COLOR_BLUE );

		// Button that enables/disables children
		enabledBtn = new Button( parent, CHECK );
		enabledBtn.setText( "" );
		enabledBtn.setSelection( optArg.getDefaultValue() );
		GridData layoutData = new GridData( LEFT, TOP, false, false, 1, 1 );
		Arg<?> childArg = optArg.getChild();
		if ( !childArg.isSimple() ) {
//			new Label( parent, NONE );
			layoutData.horizontalSpan = 2;
		}
//		GridData btnData = new GridData();
//		enabledBtn.setLayoutData( btnData );
//		setValueControl( enabledBtn );

		enabledBtn.setLayoutData( layoutData );

//		final Composite firstComp = new Composite( checkComp, NONE );
//		firstComp.setLayoutData( new GridData( FILL, FILL, true, false, 1, 1 ) );
//		firstComp.setLayout( new GridLayout( 2, false ) );
//
//		SwtStdValues.setDebugColor( firstComp, SwtStdValues.COLOR_DARK_BLUE );


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
//				SwtUtil.recursiveSetEnabled( firstComp, isEnabled() );

//				firstComp.setEnabled( true );
				enabledBtn.setEnabled( true );

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
