package j.combot.gui.visuals;

import static org.eclipse.swt.SWT.CENTER;
import static org.eclipse.swt.SWT.FILL;
import static org.eclipse.swt.SWT.LEFT;
import static org.eclipse.swt.SWT.NONE;
import static org.eclipse.swt.SWT.RADIO;
import j.combot.command.AltArg;
import j.combot.command.Arg;
import j.swt.util.SwtStdValues;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class AltVisual extends BaseArgVisual<Integer>
{
		private List<Button> buttons = new ArrayList<>();

		@SuppressWarnings( { "unchecked", "rawtypes" } )
		@Override
		public void makeWidget(
				Arg<Integer> arg, Composite parent, Button parentLbl, VisualFactory visualFactory )
		{
			AltArg altArg = (AltArg) arg;


			makeTitle( parent, parentLbl, altArg.getTitle() );
//			if ( parentLbl != null ) {
//				parentLbl.setText( altArg.getTitle() );
//				((GridData) parentLbl.getLayoutData()).horizontalSpan = 2;
//			} else {
//				// Button that enables/disables children
//				Label title = new Label( parent, NONE );
//				title.setText( altArg.getTitle() );
//				title.setLayoutData( new GridData( LEFT, CENTER, true, false, 2, 1 ) );
//			}

			Composite childsComp = new Composite( parent, NONE );
			childsComp.setLayout( new GridLayout( 2, false ) );
			GridData compData = new GridData( FILL, FILL, true, false, 2, 1 );
			compData.horizontalIndent = SwtStdValues.INDENT;
			childsComp.setLayoutData( compData );
			SwtStdValues.setDebugColor( childsComp, SwtStdValues.COLOR_RED );


			for ( Arg<?> childArg : altArg.getChildren() ) {

				// Button that enables/disables children
				final Button button = new Button( childsComp, RADIO );
				buttons.add( button );
				button.setData( childArg );

				// Child will set the text to its title, its not done here

//				enabledBtn.setSelection( optArg.getDefaultValue() );

				button.setLayoutData( new GridData( LEFT, CENTER, false, false, 1, 1 ) );


				button.addSelectionListener( new SelectionAdapter() {
					@Override public void widgetSelected( SelectionEvent e ) {
						AltVisual.setEnabledWithButton( button );
					}

				} );

				GuiArgVisual<?> childVisual = visualFactory.make( childArg );
				childVisual.makeWidget( (Arg) childArg, childsComp, button,  visualFactory );

				childVisual.setEnabled( false );
			}

			Button defaultButton = buttons.get( altArg.getDefaultValue() );
			defaultButton.setSelection( true );
			setEnabledWithButton( defaultButton );
		}

		private static void setEnabledWithButton( final Button button ) {
			( (Arg<?>) button.getData() )
				.getVisual().setEnabled( button.getSelection() );
		}

		@Override
		public void setEnabled( boolean b ) {
			enabled = b;
//			super.setEnabled( b );
			for ( Arg<?> a : ( (AltArg) getArg() ).getChildren() ) {
					a.getVisual().setEnabled( b );
			}
		}

		@Override
		public Integer getValue()
		{
			int i = 0;

			for ( Button b : buttons ) {
				if ( b.getSelection() ) return i;
				i++;
			}

			throw new IllegalStateException( "Hm. Something should be selected" );
		}



	}

