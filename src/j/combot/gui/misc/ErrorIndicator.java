package j.combot.gui.misc;

import static org.eclipse.swt.SWT.HORIZONTAL;
import static org.eclipse.swt.SWT.ICON_ERROR;
import static org.eclipse.swt.SWT.NONE;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

public class ErrorIndicator
{
	private Label errorIcon;
	private Label error;
	private Font font;

	public static Image cachedErrorIcon = null;
	public static int lastFontSize = -1;

	public ErrorIndicator() {}

	public ErrorIndicator( Composite parent ) {
		makeWidget( parent );
	}

	public void setError( String message )
	{
		errorIcon.setVisible( true );
		error.setText( message );
		error.pack();
	}

	public void clearError()
	{
		errorIcon.setVisible( false );
		error.setText( "" );
	}

	public void setFont( Font font ) {
		this.font = font;
	}

	public Control makeWidget( Composite parent )
	{
		Composite row = new Composite( parent, NONE );
		row.setLayout( new RowLayout( HORIZONTAL ) );

		errorIcon = new Label( row, NONE );
		error = new Label( row, NONE );
		error.setFont( font );
		error.pack();

		errorIcon.setImage( makeIcon( error.getSize().y, error.getDisplay() ) );
		errorIcon.setVisible( false );

		return row;
	}

	public static Image makeIcon( int size, Display display )
	{
		if ( size == lastFontSize && cachedErrorIcon != null ) {
			// If the cached icon is the right size, use that one
			return cachedErrorIcon;
		} else {
			// Else make a new icon and set that one as the cached one
			Image image = display.getSystemImage( ICON_ERROR );
			ImageData data = image.getImageData();
			data = data.scaledTo( size, size );
			cachedErrorIcon = new Image( display, data );

			display.disposeExec( new Runnable() {
				public void run() {
					cachedErrorIcon.dispose();
				}
			} );

			return cachedErrorIcon;
		}


	}


}