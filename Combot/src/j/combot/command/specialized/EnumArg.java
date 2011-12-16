package j.combot.command.specialized;

import j.combot.command.Arg;
import j.combot.gui.visuals.VisualTypes;
import j.util.util.Asserts;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EnumArg extends Arg<Integer> {

	public static class Entry {
		private final String title, name;

		public Entry( String title, String name ) {
			this.title = title;
			this.name = name;
		}

		public String getTitle() {
			return title;
		}

		public String getName() {
			return name;
		}
	}

	private List<Entry> values;

	public EnumArg( String title, Entry... vals ) {
		this( title, 0, vals );
	}

	public EnumArg( String title, int prio, Entry... vals ) {
		super( title, "" );
		setPrio( prio );
		setVisualType( VisualTypes.COMBO_TYPE );
		Asserts.arg( vals.length > 0, "There must be at least one value in an enum" );
		values = Arrays.asList( vals );
		setDefaultValue( 0 );
	}

	@Override
	public List<String> getArgStrings() {
		return Collections.singletonList(
				values.get( getVisual().getValue() ).getName() );
	}

	public List<Entry> getValues() {
		return values;
	}
}
