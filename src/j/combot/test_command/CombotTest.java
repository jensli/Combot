package j.combot.test_command;

import static java.lang.System.out;
import static org.eclipse.swt.SWT.NONE;
import static org.eclipse.swt.SWT.PUSH;
import j.combot.gui.misc.InputBox;
import j.combot.validator.LengthValidator;
import j.util.functional.Action0;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class CombotTest {

	@SuppressWarnings( "static-method" )
	public void run( Shell shell )
	{
		final Label label = new Label( shell, NONE );
		label.setText( "Shell" );

		Button b = new Button( shell, PUSH );
		b.setText( "Open" );

		final InputBox inputBox = new InputBox( shell );
		inputBox.setValidator( new LengthValidator( 3, 7 ) );
		inputBox.setMessage( "In:" );
		inputBox.setText( "Title" );
		inputBox.setResultCallback( new Action0() {
			public void run() {
				if ( inputBox.isOk() ) {
					label.setText( inputBox.getResult() );
				} else {
					label.setText( "Fel" );
				}
			}
		} );

		b.addSelectionListener( new SelectionAdapter() {
			@Override public void widgetSelected( SelectionEvent e ) {
				inputBox.open();
			}
		} );
	}

	public static void main( String[] args )
	{
		out.println( "Starting." );

		Display display = new Display();
		final Shell shell = new Shell( display );
		shell.setText( CombotTest.class.getSimpleName() );
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginWidth = 10;
		fillLayout.marginHeight = 10;
		shell.setLayout( fillLayout );

		new CombotTest().run( shell );

		shell.pack();
		shell.open();

		while ( !shell.isDisposed() ) {
			if ( !display.readAndDispatch() )
				display.sleep();
		}
		display.dispose();

		out.println( "Stopping." );
	}

}
