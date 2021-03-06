package j.combot.gui.visuals;

import java.io.File;
import java.lang.reflect.Field;


/**
 * These objects are the connection between Args and concrete ArgVisuals. Each
 * arg has one of them, and the gui has a mapping from a VisualType to a
 * ArgVisual, that decides which kind of visual a certain kind on arg gets. I
 * this way an And arg can chose which kind of visual it wants without knowing
 * anything about the contcrete visuals.
 * 
 * This is an enum with type parameters.
 */
public final class VisualTypes
{
	private VisualTypes() {}

	public static VisualType<Void>
		GROUP_TYPE,
		COMMAND_TYPE,
		COLLAPSABLE_TYPE,
		SEP_TYPE;

	public static VisualType<Object>
		CONST_TYPE;

	public static VisualType<Integer>
		INT_TYPE,
		COMBO_TYPE,
		ALT_TYPE;

	public static VisualType<String>
		STRING_TYPE,
		NULL_TYPE;

	public static VisualType<Boolean>
		OPT_TYPE;

	public static VisualType<File>
		FILE_TYPE;

	// Initialize all fields with a VisualType object
	static {
		try {
			for ( Field f : VisualTypes.class.getFields() ) {
					f.set( null, new VisualType<>( f.getName() ) );
			}
		} catch ( ReflectiveOperationException exc ) {
			throw new RuntimeException( exc );
		}
	}
}
