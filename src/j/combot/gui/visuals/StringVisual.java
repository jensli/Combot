package j.combot.gui.visuals;

import static org.eclipse.swt.SWT.BORDER;
import static org.eclipse.swt.SWT.SINGLE;
import j.combot.CmdUtil;
import j.combot.command.CommandPart;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;



public class StringVisual  extends BasePartVisual<String>
{
	private Text text;

//	@Override
//	public void makeWidget( StringArg arg, Composite parent )
//	{
//		Composite pair = new Composite( parent, NONE );
//		RowLayout pairLayout = new RowLayout( HORIZONTAL );
//		pair.setLayout( pairLayout );
//
//		final Button enabled = new Button( pair, SWT.CHECK );
//		enabled.setText( arg.getTitle() + ":" );
//
//		enabled.addSelectionListener( new SelectionAdapter() {
//			public void widgetSelected( SelectionEvent e ) {
//				text.setEnabled( enabled.getSelection() );
//			}
//		});
//
//		Label label = new Label( pair, LEFT );
//		label.setText( arg.getTitle() + ":" );
//
//		text = new Text( pair, SINGLE | BORDER );
//	}


	@Override
	protected Control makeValueWidget( CommandPart<String> part, Composite parent, Composite pair )
	{
		text = new Text( pair, SINGLE | BORDER );
		text.setText( CmdUtil.cast( part ).getDefaultValue() );
//		text.addSelectionListener( makeValidationListener() );
		text.addModifyListener( new ModifyListener() {
			@Override public void modifyText( ModifyEvent e ) {
				setValidateResult( getCommandPart().validate() );
			}
		});
		return text;
	}

	@Override
	public String getValue() {
		return text.getText();
	}

}

