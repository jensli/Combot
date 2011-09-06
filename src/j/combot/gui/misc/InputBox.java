package j.combot.gui.misc;

import static org.eclipse.swt.SWT.BORDER;
import static org.eclipse.swt.SWT.CENTER;
import static org.eclipse.swt.SWT.DEFAULT;
import static org.eclipse.swt.SWT.HORIZONTAL;
import static org.eclipse.swt.SWT.NONE;
import static org.eclipse.swt.SWT.PUSH;
import static org.eclipse.swt.SWT.SEPARATOR;
import j.combot.validator.ValEntry;
import j.combot.validator.Validator;
import j.swt.util.SwtStdValues;
import j.util.functional.Action0;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class InputBox extends Dialog
{
	private Shell dialog;
	private Text inputText;
	private Button ok;

	private ErrorIndicator errorIndicator;

	private String
		result,
		message;

	private Validator<? super String> validator = Validator.EMPTY_VALIDATOR;
	private Action0 resultCallback;

	private boolean
		isOk = false,
		trim = false;


	public InputBox( Shell parent, int style ) {
		super( parent, style );
	}

	public InputBox( Shell parent ) {
		super( parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL );
	}


	public Action0 getResultCallback() {
		return resultCallback;
	}

	public void setResultCallback( Action0 resultCallback ) {
		this.resultCallback = resultCallback;
	}

	public void open()
	{
		if ( dialog == null ) {
			make();
		}

//		dialog.open();
		dialog.setVisible( true );
		inputText.setSelection( 0, inputText.getCharCount() );
		inputText.setFocus();
		setValidateResult( false );
	}

	private void make()
	{
		dialog = new Shell( getParent(), getStyle() );
		dialog.setText( getText() );
		FormLayout formLayout = new FormLayout();
		formLayout.marginWidth = SwtStdValues.SPACING;
		formLayout.marginHeight = SwtStdValues.SPACING;
		formLayout.spacing = SwtStdValues.SPACING;
		dialog.setLayout( formLayout );

		Label label = new Label( dialog, NONE );
		label.setText( message );
		FormData data = new FormData();
		label.setLayoutData( data );

		ok = new Button( dialog, PUSH );
		ok.setText( "OK" );
		data = new FormData();
		data.width = SwtStdValues.BUTTON_WIDTH;
		data.right = new FormAttachment( 100, 0 );
		data.bottom = new FormAttachment( 100, 0 );
		ok.setLayoutData( data );

		Button cancel = new Button( dialog, PUSH );
		cancel.setText( "Cancel" );
		data = new FormData();
		data.width = SwtStdValues.BUTTON_WIDTH;
		data.right = new FormAttachment( ok, 0, DEFAULT );
		data.bottom = new FormAttachment( 100, 0 );
		cancel.setLayoutData( data );

		Label sep = new Label( dialog, SEPARATOR | HORIZONTAL );
		data = new FormData();
		data.left = new FormAttachment( 0, 0 );
		data.right = new FormAttachment( 100, 0 );
		data.bottom = new FormAttachment( cancel, 0, DEFAULT );
		sep.setLayoutData( data );
		sep.pack();

		errorIndicator = new ErrorIndicator();
		Control error = errorIndicator.makeWidget( dialog );
		data = new FormData();
		data.width = 600;
		data.left = new FormAttachment( 0, 0 );
		data.right = new FormAttachment( 100, 0 );
		data.bottom = new FormAttachment( sep, 0, DEFAULT );
		error.setLayoutData( data );

		inputText = new Text( dialog, BORDER );
		data = new FormData();
//		data.width = 200;
		data.left = new FormAttachment( label, 0, DEFAULT );
		data.right = new FormAttachment( 100, 0 );
		data.top = new FormAttachment( label, 0, CENTER );
		data.bottom = new FormAttachment( error, 0, DEFAULT );
		inputText.setLayoutData( data );


		inputText.addModifyListener( new ModifyListener() {
			@Override public void modifyText( ModifyEvent e ) {
				setValidateResult( true );
			}
		} );

		cancel.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				close( false, true );
			}
		} );

		ok.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				close( true, true );
			}
		} );

		dialog.addShellListener( new ShellAdapter() {
			@Override public void shellClosed( ShellEvent e ) {
				close( false, false );
				e.doit = false;
			}
		} );

		dialog.setDefaultButton( ok );

//		inputText.setFocus();

		dialog.pack();
		dialog.open();
	}


	private void setValidateResult( boolean updateIndicator )
	{
		List<ValEntry> errors = validator.validate( getValue() );
		ok.setEnabled( errors.isEmpty() );

		if ( updateIndicator ) {
			if ( errors.isEmpty() ) {
				errorIndicator.clearError();
			} else {
				errorIndicator.setError( errors.get( 0 ).message );
			}
		}
	}

	private String getValue() {
		return trim ? inputText.getText().trim() : inputText.getText();
	}


	private void close( boolean isOk, boolean close )
	{
		this.isOk = isOk;
		result = getValue();
//		if ( close ) dialog.close();
		dialog.setVisible( false );
		errorIndicator.clearError();
		resultCallback.run();
	}

	public Validator<? super String> getValidator() {
		return validator;
	}

	public void setValidator( Validator<? super String> validator ) {
		this.validator = validator;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage( String message ) {
		this.message = message;
	}

	public String getResult() {
		return result;
	}

	public boolean isOk() {
		return isOk;
	}

	/**
	 * Should whitespace be trimmed before it is sent to the validator of returned
	 */
	public void setTrim( boolean trim ) {
		this.trim = trim;
	}


}
