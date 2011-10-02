package j.combot.gui.misc;

import static org.eclipse.swt.SWT.HORIZONTAL;
import static org.eclipse.swt.SWT.ICON_ERROR;
import static org.eclipse.swt.SWT.ICON_INFORMATION;
import static org.eclipse.swt.SWT.ICON_WARNING;
import static org.eclipse.swt.SWT.NONE;
import j.swt.util.SwtStdValues;
import j.swt.util.SwtUtil;
import j.util.util.IssueType;
import j.util.util.Pair;

import java.util.HashMap;
import java.util.Map;

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

	// Not owned, someone else disposes it
	private Font font;

	private Image errorImage, warningImage, infoImage;
	private Composite row;

	private static Map<Pair<Integer, Integer>, Image> cachedImageMap = new HashMap<>();

	public ErrorIndicator() {}

	public ErrorIndicator( Composite parent ) {
		makeWidget( parent );
	}

	public void setIssue( IssueType type, String message )
	{

		Image i = type == IssueType.ERROR ? errorImage :
				( type == IssueType.WARNING ? warningImage : infoImage );

		errorIcon.setImage( i );
		errorIcon.setVisible( true );
		errorMsg.setText( message );
		errorMsg.pack();
	}

	public void clearIssue()
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

		errorImage = makeIcon( errorMsg.getSize().y, errorMsg.getDisplay(), ICON_ERROR );
		infoImage = makeIcon( errorMsg.getSize().y, errorMsg.getDisplay(), ICON_INFORMATION );
		warningImage = makeIcon( errorMsg.getSize().y, errorMsg.getDisplay(), ICON_WARNING );

		errorIcon.setImage( errorImage );
		errorIcon.setVisible( false );

		return row;
	}

	// DO: Move this to a Swt util class?
	public static Image makeIcon( int size, Display display, int img )
	{
		Pair<Integer, Integer> p = new Pair<>( img, size );

		// Check if we have created an image of right type and size earlier
		Image i = cachedImageMap.get( p );

		if ( i == null ) {
			// Else make a new icon and set that one as the cached one
			Image image = display.getSystemImage( img );
			ImageData data = image.getImageData();

			data = data.scaledTo( size, size );
			final Image newI = new Image( display, data );

			cachedImageMap.put( p, newI );

			display.disposeExec( new Runnable() {
				public void run() {
					newI.dispose();
				}
			} );

			i = newI;
		}

		return i;
	}

	public void dispose() {
		if ( row != null ) {
			row.dispose();
		}
	}


}
