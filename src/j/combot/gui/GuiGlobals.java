package j.combot.gui;

import static org.eclipse.swt.SWT.COLOR_BLUE;
import static org.eclipse.swt.SWT.COLOR_RED;
import static org.eclipse.swt.SWT.ICON_ERROR;
import j.combot.gui.visuals.CommandVisual;
import j.combot.gui.visuals.GuiPartVisual;
import j.combot.gui.visuals.IntVisual;
import j.combot.gui.visuals.NullVisual;
import j.combot.gui.visuals.OptVisual;
import j.combot.gui.visuals.StringVisual;
import j.combot.gui.visuals.VisFact;
import j.combot.gui.visuals.VisualTypes;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

public final class GuiGlobals
{
	public static Image ERROR_ICON;


	private GuiGlobals() {}

	public static Font
		TITLE_FONT,
		SMALL_FONT,
		BIG_FONT;

	public static Color
		RED,
    	BLUE;


	public static void init( Display display )
	{
	    RED = display.getSystemColor( COLOR_RED );
		BLUE = display.getSystemColor( COLOR_BLUE );
		TITLE_FONT = makeTitleFont( display.getSystemFont(), 1.3, SWT.BOLD, display );
		SMALL_FONT = makeTitleFont( display.getSystemFont(), 0.9, SWT.NONE, display );
		BIG_FONT = makeTitleFont( display.getSystemFont(), 1.3, SWT.NONE, display );

		display.disposeExec( new Runnable() {
			public void run() {
				TITLE_FONT.dispose();
				SMALL_FONT.dispose();
				BIG_FONT.dispose();
			}
		} );
	}

	public static Font makeTitleFont( Font sysFont, double mult, int style,  Device device )
	{
		FontData[] fontData = sysFont.getFontData();

		for ( FontData fd : fontData ) {
			fd.height = ( Math.round( fd.height*mult ) );
			fd.style |= style;
			fd.string = null;
		}

		return new Font( device, fontData );
	}

//	public static Font makeTitleFont( Control c ) {
//		return makeTitleFont( c.getFont(), c.getDisplay() );
//	}





	public static final VisFactEntry<?>[] VIS_FACTS = new VisFactEntry[] {

		new VisFactEntry<Integer>(
				VisualTypes.STD_INT_TYPE,
				new VisFact<Integer>() {
					public GuiPartVisual<Integer> make() {
						return new IntVisual();
					}
				} ),

		new VisFactEntry<String>(
				VisualTypes.STD_STRING_TYPE, new VisFact<String>() {
					public GuiPartVisual<String> make() {
						return new StringVisual(); }
				} ),

		new VisFactEntry<String>(
				VisualTypes.STD_COMMAND_TYPE,
				new VisFact<String>() {
					public GuiPartVisual<String> make() { return new CommandVisual(); }
				} ),

		new VisFactEntry<String>(
				VisualTypes.STD_NULL_TYPE,
				new VisFact<String>() {
					public GuiPartVisual<String> make() { return new NullVisual(); }
				} ),

		new VisFactEntry<List<Object>>(
				VisualTypes.STD_OPT_TYPE,
				new VisFact<List<Object>>() {
					public GuiPartVisual<List<Object>> make() { return new OptVisual(); }
				} ),

	};


	public static void initImage( int size, Display display )
	{
		if ( ERROR_ICON != null ) return;
	
		Image image = display.getSystemImage( ICON_ERROR );
		ImageData data = image.getImageData();
		data = data.scaledTo( size, size );
		ERROR_ICON = new Image( display, data );
	
		display.disposeExec( new Runnable() {
			public void run() {
				ERROR_ICON.dispose();
			}
		} );
	}


}
