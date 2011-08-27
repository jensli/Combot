package j.combot.gui.misc;

import j.combot.command.Arg;
import j.combot.command.Validator;
import j.combot.gui.visuals.ArgVisual;
import j.combot.gui.visuals.GuiArgVisual;
import j.combot.gui.visuals.VisualFactory;
import j.util.util.Boxed;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public final class GuiUtil
{
	private GuiUtil() {}

	public static void createVisuals( List<Arg<?>> args, Composite comp,
			VisualFactory visualFactory )
	{
		for ( Arg<?> arg : args ) {
			createVisual( arg, comp, visualFactory);
		}
	}

	@SuppressWarnings( { "unchecked", "rawtypes" } )
	public static void createVisual( Arg<?> arg, Composite comp, VisualFactory visualFactory )
	{
		GuiArgVisual<?> visual = visualFactory.make( arg );
		visual.setArg( (Arg) arg );
		arg.setVisual( (ArgVisual) visual );
		visual.makeWidget( (Arg) arg, comp, visualFactory );
	}

	public static String openStringBox( final Shell shell, String title,
			String message, Validator<String> validator )
	{
		final Shell dialog = new Shell( shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL );
		dialog.setText( title );
		FormLayout formLayout = new FormLayout();
		formLayout.marginWidth = 10;
		formLayout.marginHeight = 10;
		formLayout.spacing = 10;
		dialog.setLayout( formLayout );

		Label label = new Label( dialog, SWT.NONE );
		label.setText( message );
		FormData data = new FormData();
		label.setLayoutData( data );

		Button cancel = new Button( dialog, SWT.PUSH );
		cancel.setText( "Cancel" );
		data = new FormData();
		data.width = 60;
		data.right = new FormAttachment( 100, 0 );
		data.bottom = new FormAttachment( 100, 0 );
		cancel.setLayoutData( data );

		final Text text = new Text( dialog, SWT.BORDER );
		data = new FormData();
		data.width = 200;
		data.left = new FormAttachment( label, 0, SWT.DEFAULT );
		data.right = new FormAttachment( 100, 0 );
		data.top = new FormAttachment( label, 0, SWT.CENTER );
		data.bottom = new FormAttachment( cancel, 0, SWT.DEFAULT );
		text.setLayoutData( data );


		Button ok = new Button( dialog, SWT.PUSH );
		ok.setText( "OK" );
		data = new FormData();
		data.width = 60;
		data.right = new FormAttachment( cancel, 0, SWT.DEFAULT );
		data.bottom = new FormAttachment( 100, 0 );
		ok.setLayoutData( data );

		final Boxed<String> result = new Boxed<>();

		cancel.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				System.out.println( "User cancelled dialog" );
				dialog.close();
			}
		} );

		ok.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				System.out.println( "User typed: " + text.getText() );
				result.value = text.getText();
				dialog.close();
			}
		} );

		dialog.setDefaultButton( ok );
		dialog.pack();
		dialog.open();

		return result.value;
	}


}
