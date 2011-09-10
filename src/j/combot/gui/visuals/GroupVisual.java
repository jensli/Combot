package j.combot.gui.visuals;

import static org.eclipse.swt.SWT.FILL;
import static org.eclipse.swt.SWT.NONE;
import j.combot.command.Arg;
import j.combot.command.ComposedArg;
import j.swt.util.SwtStdValues;
import j.util.util.NotImplementedException;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class GroupVisual extends BaseArgVisual<Object>
{
	@Override
	public Boolean getValue() {
		throw new NotImplementedException();
	}

	@Override
	public void makeWidget(
			Arg<Object> part, Composite parent, Composite childrenParent, VisualFactory visualFactory )
	{
		ComposedArg compArg = (ComposedArg) part;

		// Button that enables/disables children
		Label title = new Label( parent, NONE );
		title.setText( compArg.getTitle() );
		GridData titleData = new GridData();
		titleData.horizontalSpan = 2;
		title.setLayoutData( titleData );

		// Add panel for children
		final Composite childsComp = new Composite( parent, NONE );
		childsComp.setLayout( new GridLayout( 2, false ) );
		GridData compData = new GridData( FILL, FILL, true, false );
		compData.horizontalSpan = 2;
		compData.horizontalIndent = (int) ( SwtStdValues.UNIT * 1.5 );
		childsComp.setLayoutData( compData );
//		childsComp.setBackground( SwtStdValues.RED );

		// Loop over children and add them reursivly.
		for ( Arg<?> arg : compArg.getArgGroup() ) {
			visualFactory.make( arg ).makeWidget( (Arg) arg, childsComp, null, visualFactory );
		}

		setValueControl( childsComp );
//		SwtUtil.recursiveSetEnabled( childsComp, isEnabled() );
	}


}
