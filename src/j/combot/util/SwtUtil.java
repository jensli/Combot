package j.combot.util;

import static org.eclipse.swt.SWT.ICON_ERROR;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.TreeItem;


@Retention( RetentionPolicy.RUNTIME )
@interface AutoLoadColor {}

@Retention( RetentionPolicy.RUNTIME )
@interface LoadColor {
    int value();
}


public final class SwtUtil
{
    private static final boolean USE_DEBUG_COLORS = false;

    public static Image ERROR_ICON;


    private SwtUtil() {}


    public static Font
        TITLE_FONT,
        SMALL_FONT,
        BIG_FONT;

    @AutoLoadColor
    public static Color
        COLOR_RED,
        COLOR_BLUE,
        COLOR_DARK_GREEN,
        COLOR_DARK_BLUE,
        COLOR_DARK_YELLOW,
        COLOR_YELLOW;

    @LoadColor( SWT.COLOR_DARK_CYAN )
    public static Color DARK_CYAN;

    public static int
        FONT_HEIGHT,
        FONT_WIDTH,
        UNIT,
        INDENT,
        SPACING,
        BUTTON_WIDTH;

    private static final List<Color> privColors = new ArrayList<>();

    public static final List<Color> COLORS = Collections.unmodifiableList( privColors ) ;


    public static void setDebugColor( Control ctrl, Color c  )
    {
        if ( USE_DEBUG_COLORS ) {
            ctrl.setBackground( c );
        }
    }

    /*
     * Use reflection to get system colors from the display, initialize color
     * fields in this class.
     */
    private static void loadColors( Display display )
    {
        try {

            for ( Field f : SwtUtil.class.getFields() ) {
                if ( !Objects.equals( f.getType(), Color.class ) ) continue;

                if ( f.isAnnotationPresent( AutoLoadColor.class ) ) {

                    int constValue = (int) SWT.class.getField( f.getName() ).get( null );
                    Color c = display.getSystemColor( constValue );

                    f.set( null, c );
                    privColors.add( c );

                } else if ( f.isAnnotationPresent( LoadColor.class ) ) {
                    Color c = display.getSystemColor( f.getAnnotation( LoadColor.class ).value() );
                    f.set( null, c );
                    privColors.add( c );
                }
            }

        } catch ( ReflectiveOperationException exc ) {
            exc.printStackTrace();
            throw new RuntimeException( exc );
        }
    }

    public static void init( Display display )
    {
        loadColors( display );

        TITLE_FONT = makeTitleFont( display.getSystemFont(), 1.3, SWT.BOLD, display );
        SMALL_FONT = makeTitleFont( display.getSystemFont(), 0.9, SWT.NONE, display );
        BIG_FONT = makeTitleFont( display.getSystemFont(), 1.3, SWT.NONE, display );


        GC gc = new GC( display );
        FONT_HEIGHT = gc.getFontMetrics().getHeight();
        FONT_WIDTH = gc.getFontMetrics().getAverageCharWidth();
        UNIT = FONT_HEIGHT;
        INDENT = UNIT * 2;
        SPACING = UNIT / 2;
        BUTTON_WIDTH = UNIT*5;
        gc.dispose();


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

    // TODO: This enables/disables ALL children, should only be
    // parent. When switching back, children should regain their
    // previous state, not all of them should be enabled.
    //
    // Make a solution where prev state is saved with setData(...) ?
    public static void recursiveSetEnabled( Control ctrl, boolean enabled )
    {
        ctrl.setEnabled( enabled );
        if ( ctrl instanceof Composite ) {
            Composite comp = (Composite) ctrl;
            for ( Control c : comp.getChildren() ) {
                recursiveSetEnabled( c, enabled );
            }
        }
    }

    public static void scrollToMax( ScrollBar bar ) {
        bar.setSelection( Integer.MAX_VALUE );
    }

    public static void appendStyled( StyledText text, String s, Color color )
    {
        int size = text.getCharCount();
        text.append( s );
        text.setStyleRange( new StyleRange( size, s.length(), color, null ) );
    }

    public static <T> List<T> getData( TreeItem[] items, Class<T> cls )
    {
        List<T> result = new ArrayList<>();
        for ( TreeItem i : items ) {
            result.add( cls.cast( i.getData() ) );
        }
        return result;
    }


}
