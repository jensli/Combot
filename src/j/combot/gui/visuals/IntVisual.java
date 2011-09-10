package j.combot.gui.visuals;

import static org.eclipse.swt.SWT.BORDER;
import j.combot.command.Arg;
import j.combot.command.IntArg;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;



public class IntVisual extends BaseArgVisual<Integer> {

	private Spinner spinner;


	@Override
	protected Control makeValueWidget( Arg<Integer> part, Composite parent,
			Composite pair )
	{
		IntArg arg = (IntArg) part ;
		spinner = new Spinner( pair, BORDER );
		spinner.setSelection( arg.getDefaultValue() );
		spinner.setMaximum( arg.getMax() );
		spinner.setMinimum( arg.getMin() );

		spinner.addSelectionListener( makeValidationListener() );
		return spinner;
	}


	@Override
	public Integer getValue() {
		return Integer.parseInt( spinner.getText() );
	}


}
