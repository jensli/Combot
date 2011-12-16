package j.combot.gui.visuals.specialized;

import static org.eclipse.swt.SWT.BORDER;
import static org.eclipse.swt.SWT.FILL;
import static org.eclipse.swt.SWT.NONE;
import static org.eclipse.swt.SWT.PUSH;
import static org.eclipse.swt.SWT.SINGLE;
import j.combot.command.Arg;
import j.combot.command.specialized.FileArg;
import j.combot.gui.visuals.BaseArgVisual;
import j.combot.util.SwtUtil;
import j.combot.validator.ValEntry;

import java.io.File;
import java.util.List;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;


public class FileVisual extends BaseArgVisual<File>
{
	private Text text;
	private FileDialog fileDialog;
	private DirectoryDialog directoryDialog;

	@Override
	protected Control makeValueWidget( Arg<File> arg, Composite parent )
	{
		Composite fileComp = new Composite( parent, NONE );
		fileComp.setLayoutData( new GridData( FILL, FILL, true, false ) );
		fileComp.setLayout( new GridLayout( 2, false ) );

		text = new Text( fileComp, SINGLE | BORDER );
		text.setText( getArg().getDefaultValue().toString() );

		text.setLayoutData( new GridData( FILL, FILL, true, false ) );

		text.addModifyListener( new ModifyListener() {
			@Override public void modifyText( ModifyEvent e ) {
				List<ValEntry> validate = getArg().validate();
				setValidateResult( validate );
			}
		});

		Button browseButton = new Button( fileComp, PUSH );
		browseButton.setText( "Browse..." );
		GridData buttonData = new GridData( FILL, FILL, false, false );
		buttonData.widthHint = SwtUtil.BUTTON_WIDTH;
		browseButton.setLayoutData( buttonData );


		browseButton.addSelectionListener( new SelectionAdapter() {
			@Override public void widgetSelected( SelectionEvent e ) {
				openDialog();
			}
		} );

		return fileComp;
	}

	@Override
	public File getValue() {
		return new File( text.getText() );
	}

	private void openDialog()
	{
		String result;

		switch ( ((FileArg) getArg()).getDialogType() ) {
			case DIR:
				if ( directoryDialog == null ) {
					directoryDialog = new DirectoryDialog( text.getShell() );
				}

				result = directoryDialog.open();
				break;

			case FILE:

				if ( fileDialog == null ) {
					fileDialog = new FileDialog( text.getShell() );
				}

				result = fileDialog.open();
				break;

			default: throw new IllegalStateException();
		}

		if ( result != null ) {
			text.setText( result );
		}

	}


}
