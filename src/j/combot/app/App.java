package j.combot.app;


public interface App
{
	public enum RunMode {
		DEBUG, NORMAL,
	}

	public enum StartMode {
		START, RESTART,
	}

	public enum ExitCode {
		STOP, RESTART,
	}

	public void init( StartMode mode );
	public ExitCode run() throws Exception;
	public void dispose( ExitCode exitCode );

}


