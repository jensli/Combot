package j.combot.gui.visuals;

import java.lang.reflect.Field;


/**
 * These objects are the connection between Args and concrete ArgVisuals. Each
 * arg has one of them, and the gui has a mapping from a VisualType to a
 * ArgVisual, that decides which kind of visual a certain kind on arg gets. I
 * this way an And arg can chose which kind of visual it wants without knowing
 * anything about the contcrete visuals.
 */
public final class VisualTypes
{
	private VisualTypes() {}

	public static VisualType<Integer>
		INT_TYPE;

	public static VisualType<String>
		STRING_TYPE,
		COMMAND_TYPE,
		NULL_TYPE;

	public static VisualType<Boolean>
		OPT_TYPE;

	static {
		try {
			for ( Field f : VisualTypes.class.getFields() ) {
					f.set( null, new VisualType<>( f.getName() ) );
			}
		} catch ( IllegalArgumentException | IllegalAccessException exc ) {
			exc.printStackTrace();
			throw new RuntimeException( exc );
		}
	}
}
