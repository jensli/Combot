package j.combot.gui.visuals.specialized;

import static org.eclipse.swt.SWT.BORDER;
import static org.eclipse.swt.SWT.FILL;
import static org.eclipse.swt.SWT.SINGLE;
import j.combot.command.Arg;
import j.combot.gui.visuals.BaseArgVisual;
import j.combot.validator.ValEntry;

import java.util.List;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;



public class StringVisual  extends BaseArgVisual<String>
{
	private Text text;

	@Override
	protected Control makeValueWidget( Arg<String> arg, Composite parent )
	{
		text = new Text( parent, SINGLE | BORDER );
		text.setText( arg.getDefaultValue() );
		text.setLayoutData( new GridData( FILL, FILL, true, false ) );

		text.addModifyListener( new ModifyListener() {
			@Override public void modifyText( ModifyEvent e ) {
				List<ValEntry> validate = getArg().validate();
				setValidateResult( validate );
			}
		});

		return text;
	}

	@Override
	public String getValue() {
		return text.getText();
	}

}

