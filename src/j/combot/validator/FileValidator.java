package j.combot.validator;

import j.combot.command.specialized.FileArg.DialogType;
import j.combot.command.specialized.FileArg.Val;
import j.util.util.IssueType;

import java.io.File;
import java.util.List;

public class FileValidator extends Validator<File> {

	private final Val val;
	private final DialogType dialogType;

	public FileValidator( Val val, DialogType type ) {
		this.val = val;
		this.dialogType = type;
	}

	@Override
	protected List<ValEntry> validateInt( File value )
	{
		IssueType type = val.error ? IssueType.ERROR : IssueType.WARNING;
		String dirOrFile = dialogType == DialogType.FILE ? "File" : "Directory",
				message = null;

		if ( value.toString().isEmpty() ) {
			return standardCreateList( false, "This value can not be empty" );
		}

		boolean exists = value.exists(),
				isOk = val.exists ? !exists : exists;

		message = dirOrFile + ( val.exists ? " already exist" : " does not exist" );

		return standardCreateList( isOk, message, type );
	}


}


