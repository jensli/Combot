package j.combot.gui.visuals;

import java.lang.reflect.Field;
import java.util.List;


public final class VisualTypes
{
	private VisualTypes() {}

	public static VisualType<Integer> STD_INT_TYPE;
	public static VisualType<String> STD_STRING_TYPE;
	public static VisualType<String> STD_COMMAND_TYPE;
	public static VisualType<List<Object>> STD_OPT_TYPE;
	public static VisualType<String> STD_NULL_TYPE;


	static {
		try {
		Field[] fs = VisualTypes.class.getFields();

		for ( Field f : fs ) {
				f.set( null, new VisualType<Object>( f.getName() ) );
		}

		} catch ( IllegalArgumentException exc ) {
			exc.printStackTrace();
			throw new RuntimeException( exc );
		} catch ( IllegalAccessException exc ) {
			exc.printStackTrace();
			throw new RuntimeException( exc );
		}
	}
}
