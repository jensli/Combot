package j.combot.command.specialized;

import j.combot.command.Arg;
import j.combot.gui.visuals.VisualTypes;
import j.combot.validator.FileValidator;

import java.io.File;
import java.util.Collections;
import java.util.List;


public class FileArg extends Arg<File>
{
	private DialogType dialogType;

	public FileArg( String title, String name, String def, DialogType type, Val val )
	{
		super( title, name, new File( def ), new FileValidator( val, type ) );

		setVisualType( VisualTypes.FILE_TYPE );
		this.dialogType = type;
	}

	@Override
	public List<String> getArgStrings() {
		return Collections.singletonList( getVisual().getValue().toString() );
	}

	public enum DialogType {
		FILE, DIR
	}

	public enum Val
	{
		ERR_EX( true, true ),
		WARN_EX( true, false ),
		ERR_NO_EX( false, true ),
		WARN_NO_EX( false, false ),
		NO_VAL( false, false );

		public final boolean exists, error;

		private Val( boolean exists, boolean error ) {
			this.exists = exists;
			this.error = error;
		}
	}

	public DialogType getDialogType() {
		return dialogType;
	}



}
