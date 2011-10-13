package j.combot.gui.visuals.specialized;

import static org.eclipse.swt.SWT.DROP_DOWN;
import static org.eclipse.swt.SWT.READ_ONLY;
import j.combot.command.Arg;
import j.combot.command.specialized.EnumArg;
import j.combot.command.specialized.EnumArg.Value;
import j.combot.gui.visuals.BaseArgVisual;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ComboVisual extends BaseArgVisual<Integer>
{
	private Combo combo;

	@Override
	public Integer getValue() {
		return combo.getSelectionIndex();
	}

	@Override
	protected Control makeValueWidget( Arg<Integer> arg, Composite parent )
	{
		EnumArg enumArg = (EnumArg) arg;

		combo = new Combo( parent, READ_ONLY | DROP_DOWN );

		for ( Value s : enumArg.getValues() ) {
			combo.add( s.getTitle() );
		}

		combo.select( enumArg.getDefaultValue() );

		return combo;
	}

}
