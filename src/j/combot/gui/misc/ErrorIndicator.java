package j.combot.gui.misc;

import static org.eclipse.swt.SWT.HORIZONTAL;
import static org.eclipse.swt.SWT.ICON_ERROR;
import static org.eclipse.swt.SWT.NONE;
import j.swt.util.SwtStdValues;
import j.swt.util.SwtUtil;

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
	private Label errorMsg;
	private Font font;

	public static Image cachedErrorIcon = null;
	public static int lastFontSize = -1;
	private Composite row;

	public ErrorIndicator() {}

	public ErrorIndicator( Composite parent ) {
		makeWidget( parent );
	}

	public void setError( String message )
	{
		errorIcon.setVisible( true );
		errorMsg.setText( message );
		errorMsg.pack();
	}

	public void clearError()
	{
		errorIcon.setVisible( false );
		errorMsg.setText( "" );
	}

	public void setFont( Font font ) {
		this.font = font;
	}

	public boolean isEnabled() {
		return row.getEnabled();
	}

	public void setEnabled( boolean b ) {
		SwtUtil.recursiveSetEnabled( row, b );
	}

	public Control makeWidget( Composite parent )
	{
		row = new Composite( parent, NONE );
		row.setLayout( new RowLayout( HORIZONTAL ) );
		SwtStdValues.setDebugColor( row, SwtStdValues.COLOR_DARK_BLUE );

		errorIcon = new Label( row, NONE );
		errorMsg = new Label( row, NONE );
		errorMsg.setFont( font );
		errorMsg.pack();

		errorIcon.setImage( makeIcon( errorMsg.getSize().y, errorMsg.getDisplay() ) );
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
