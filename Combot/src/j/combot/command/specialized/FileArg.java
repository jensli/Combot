package j.combot.command.specialized;

import j.combot.command.Arg;
import j.combot.gui.visuals.VisualTypes;
import j.combot.validator.FileValidator;
import j.combot.validator.FileValidator.FileVal;
import j.util.util.FileOrDir;
import j.util.util.Util;

import java.io.File;
import java.util.Collections;
import java.util.List;


public class FileArg extends Arg<File>
{
	private FileOrDir dialogType;

	public FileArg( String title, String name, String def, FileOrDir type, FileVal val )
	{
		super( title, name, new File( def ), new FileValidator( val, type ) );

		setVisualType( VisualTypes.FILE_TYPE );
		this.dialogType = type;
	}

	@Override
	public List<String> getArgStrings() {
		return Collections.singletonList( Util.expandHome( getVisual().getValue().toString() ) );
	}

	public FileOrDir getDialogType() {
		return dialogType;
	}



}
