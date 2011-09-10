package j.combot.gui.visuals;

import static org.eclipse.swt.SWT.FILL;
import static org.eclipse.swt.SWT.NONE;
import j.combot.command.Arg;
import j.combot.command.CompositeArg;
import j.swt.util.SwtStdValues;
import j.util.util.NotImplementedException;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class CompositeVisual extends BaseArgVisual<Object>
{
	private Composite childsComp;

	@Override
	public Boolean getValue() {
		throw new NotImplementedException();
	}


	@SuppressWarnings( { "unchecked", "rawtypes" } )
	@Override
	public void makeWidget( Arg<Object> part, Composite parent, Button parentLbl, VisualFactory visualFactory )
	{
		CompositeArg compArg = (CompositeArg) part;

		if ( parentLbl != null ) {
			parentLbl.setText( compArg.getTitle() );
		} else {
			// Button that enables/disables children
			Label title = new Label( parent, NONE );
			title.setText( compArg.getTitle() );
			GridData titleData = new GridData();
			titleData.horizontalSpan = 2;
			title.setLayoutData( titleData );
		}

		childsComp = new Composite( parent, NONE );
		childsComp.setLayout( new GridLayout( 2, false ) );
		GridData compData = new GridData( FILL, FILL, true, false );
		compData.horizontalSpan = 2;
		compData.horizontalIndent = (int) ( SwtStdValues.UNIT * 1.5 );
		childsComp.setLayoutData( compData );

		SwtStdValues.setDebugColor( childsComp, SwtStdValues.COLOR_RED );

		// Loop over children and add them reursivly.
		for ( Arg<?> arg : compArg.getArgGroup() ) {
			visualFactory.make( arg ).makeWidget( (Arg) arg, childsComp, visualFactory );
		}
	}


	@Override
	public void setEnabled( boolean b )
	{
		super.setEnabled( b );
		childsComp.setEnabled( b );
		for ( ArgVisual<?> v : ((CompositeArg) getArg()).getArgGroup().getVisuals() ) {
			v.setEnabled( b );
		}
	}



}
