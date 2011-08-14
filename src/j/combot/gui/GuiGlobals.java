package j.combot.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public final class GuiGlobals
{
	private GuiGlobals() {}


	private static Font titleFont;


	public static void initTitleFont( Display display )
	{
		titleFont = makeTitleFont( display.getSystemFont(), display );
	}

	public static Font makeTitleFont( Font sysFont, Device device )
	{
		FontData[] fontData = sysFont.getFontData();

		for ( FontData fd : fontData ) {
			fd.height = ( Math.round( fd.height*1.3 ) );
			fd.style |= SWT.BOLD;
			fd.string = null;
		}

		return new Font( device, fontData );
	}

	public static Font makeTitleFont( Control c ) {
		return makeTitleFont( c.getFont(), c.getDisplay() );
	}


	public static Font getTitleFont() {
		return titleFont;
	}

}
