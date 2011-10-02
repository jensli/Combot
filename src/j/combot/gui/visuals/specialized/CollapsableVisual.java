package j.combot.gui.visuals.specialized;

import static java.lang.System.out;
import static org.eclipse.swt.SWT.FILL;
import static org.eclipse.swt.SWT.NONE;
import j.combot.command.Arg;
import j.combot.command.specialized.CollapsableArg;
import j.combot.gui.visuals.BaseArgVisual;
import j.combot.gui.visuals.GuiArgVisual;
import j.combot.gui.visuals.VisualFactory;
import j.swt.util.SwtStdValues;

import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ExpandEvent;
import org.eclipse.swt.events.ExpandListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;

public class CollapsableVisual extends BaseArgVisual<Void> {

	@Override
	public Void getValue() {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings( { "unchecked", "rawtypes" } )
	@Override
	public void makeWidget( Arg<Void> arg, final Composite parent,
		Button parentLabel, VisualFactory visualFactory ) {
		CollapsableArg collArg = (CollapsableArg) arg;

		final ExpandBar expandBar = new ExpandBar( parent, NONE );
		expandBar.setLayoutData( new GridData( FILL, FILL, true, true, 2, 1 ) );

		ExpandItem item = new ExpandItem( expandBar, NONE );
		item.setText( collArg.getTitle() );
		item.setExpanded( true );

		ExpandItem i2 = new ExpandItem( expandBar, NONE );
		i2.setText( "Blppp" );

		new Label( expandBar, NONE );

		Composite stupidExpComp = new Composite( expandBar, NONE );
		stupidExpComp.setLayout( new GridLayout( 2, false ) );
		SwtStdValues.setDebugColor( stupidExpComp, SwtStdValues.COLOR_DARK_YELLOW );

		new Label( stupidExpComp, NONE ).setText( "Grrr" );
		new Label( stupidExpComp, NONE ).setText( "Grrr" );


		expandBar.addControlListener( new ControlAdapter() {
			public void controlMoved( ControlEvent e ) {
				out.println("asdf");
			}
			public void controlResized( ControlEvent e ) {
				out.println("asdf");
			}
		} );

		expandBar.addExpandListener( new ExpandListener() {
			public void itemExpanded( ExpandEvent e ) {
				expandBar.pack();
				parent.getShell().layout();
//				parent.pack();

			}
			public void itemCollapsed( ExpandEvent e ) {
				expandBar.pack();
				parent.getShell().layout();
//				parent.pack();
			}
		} );

		item.setControl( stupidExpComp );

		Arg<?> childArg = collArg.getChild();
		GuiArgVisual<?> childVisual = visualFactory.make( childArg );
		childVisual.makeWidget( (Arg) childArg, stupidExpComp, visualFactory );
	}






}
