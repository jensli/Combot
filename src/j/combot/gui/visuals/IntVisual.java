package j.combot.gui.visuals;

import static org.eclipse.swt.SWT.BORDER;
import j.combot.command.CommandPart;
import j.combot.command.IntArg;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;



public class IntVisual extends BasePartVisual<Integer> {

	private Spinner spinner;

//	@Override
//	public void makeWidget( IntArg arg, Composite parent )
//	{
//		Composite pair = new Composite( parent, NONE );
//		RowLayout pairLayout = new RowLayout( HORIZONTAL );
//		pair.setLayout( pairLayout );
//
//
////		Button enabled = new Button( pair, CHECK );
////		enabled.setText( arg.getTitle() + ":" );
//		Label label = new Label( pair, SWT.LEFT );
//		label.setText( arg.getTitle() + ":" );
//
//		spinner = new Spinner( pair, BORDER );
//		spinner.setMaximum( arg.getMax() );
//		spinner.setMinimum( arg.getMin() );
//	}



	@Override
	protected Control makeValueWidget( CommandPart<Integer> part, Composite parent,
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
