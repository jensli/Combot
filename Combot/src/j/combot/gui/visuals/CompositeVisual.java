package j.combot.gui.visuals;

import static org.eclipse.swt.SWT.FILL;
import static org.eclipse.swt.SWT.NONE;
import j.combot.command.Arg;
import j.combot.command.CompositeArg;
import j.combot.util.SwtUtil;
import j.util.util.NotImplementedException;

import java.util.List;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class CompositeVisual extends BaseArgVisual<Void>
{
	private Composite childsComp;

	@Override
	public Void getValue() {
		throw new NotImplementedException();
	}

	@SuppressWarnings( { "unchecked", "rawtypes" } )
	@Override
	public void makeWidget( Arg<Void> arg, Composite parent, Button parentLbl, VisualFactory visualFactory )
	{
		makeTitle( parent, parentLbl, arg.getTitle() );

		childsComp = new Composite( parent, NONE );
		GridData compData = new GridData( FILL, FILL, true, false, 2, 1 );
		compData.horizontalIndent = SwtUtil.INDENT;
		childsComp.setLayoutData( compData );

		childsComp.setLayout( new GridLayout( 2, false ) );
		SwtUtil.setDebugColor( childsComp, SwtUtil.DARK_CYAN );

		// Loop over children and add them reursivly.
		for ( Arg<?> child : ((CompositeArg) arg).getArgGroup() ) {
			visualFactory.make( child ).makeWidget( (Arg) child, childsComp, visualFactory );
		}
	}


	@Override
	public void setEnabled( boolean b )
	{
		enabled = b;
		childsComp.setEnabled( b );

		List<ArgVisual<?>> childs = ( (CompositeArg) getArg() ).getArgGroup().getVisuals();

		for ( ArgVisual<?> v : childs ) {
			v.setEnabled( b );
		}
	}



}
