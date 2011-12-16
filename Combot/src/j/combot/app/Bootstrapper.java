package j.combot.app;


public interface Bootstrapper
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

	public void init( StartMode mode, String[] args );
	public ExitCode run() throws Exception;
	public void dispose( ExitCode exitCode );

}


