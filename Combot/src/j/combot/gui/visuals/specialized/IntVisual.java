package j.combot.gui.visuals.specialized;

import static org.eclipse.swt.SWT.BORDER;
import static org.eclipse.swt.SWT.FILL;
import j.combot.command.Arg;
import j.combot.command.specialized.IntArg;
import j.combot.gui.visuals.BaseArgVisual;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;



public class IntVisual extends BaseArgVisual<Integer> {

	private Spinner spinner;


	@Override
	protected Control makeValueWidget( Arg<Integer> part, Composite parent )
	{
		IntArg arg = (IntArg) part;
		spinner = new Spinner( parent, BORDER );
		spinner.setSelection( arg.getDefaultValue() );
		spinner.setMaximum( arg.getMax() );
		spinner.setMinimum( arg.getMin() );

		spinner.addSelectionListener( makeValidationListener() );
		spinner.setLayoutData( new GridData( FILL, FILL, false, false ) );

		return spinner;
	}


	@Override
	public Integer getValue() {
		return Integer.parseInt( spinner.getText() );
	}


}
