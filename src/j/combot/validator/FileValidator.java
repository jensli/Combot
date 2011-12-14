package j.combot.validator;

import j.util.util.FileOrDir;
import j.util.util.IssueType;
import j.util.util.Util;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class FileValidator extends Validator<File> {

	private final FileVal val;
	private final FileOrDir dialogType;

	private boolean tildeForHome = true;

	/**
	 * Decides what validation strategy to use. Check if the file exists or
	 * not exist, and issue a warning or an error.
	 */
	public enum FileVal
	{
		ERROR_EXIST( true, true ),
		WARN_EXIST( true, false ),

		ERROR_NOT_EXIST( false, true ),
		WARN_NOT_EXIST( false, false ),

		NO_VALIDATION( false, false );

		public final boolean exists, error;

		private FileVal( boolean exists, boolean error ) {
			this.exists = exists;
			this.error = error;
		}
	}

	public void setTildeForHome( boolean tildeForHome ) {
        this.tildeForHome = tildeForHome;
    }

    public FileValidator( FileVal val, FileOrDir type ) {
		this.val = val;
		this.dialogType = type;
	}

	@Override
	protected List<ValEntry> validateInt( File value )
	{
	    if ( tildeForHome ) {
	        value = new File( Util.expandHome( value.toString() ) );
	    }

		if ( val == FileVal.NO_VALIDATION ) return Collections.emptyList();

		if ( value.toString().isEmpty() ) {
			return standardCreateList( false, "This value can not be empty" );
		}

		IssueType type = val.error ? IssueType.ERROR : IssueType.WARNING;

		boolean //exists = value.exists(),
				isOk = val.exists != value.exists(); //? !exists : exists;  //

		String dirOrFile = dialogType == FileOrDir.FILE ? "File" : "Directory",
				message;

		message = dirOrFile + ( val.exists ? " already exists" : " does not exist" );

		return standardCreateList( isOk, message, type );
	}

}



