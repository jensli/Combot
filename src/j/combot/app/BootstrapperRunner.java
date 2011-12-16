package j.combot.app;

import j.combot.app.Bootstrapper.ExitCode;
import j.combot.app.Bootstrapper.RunMode;
import j.combot.app.Bootstrapper.StartMode;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.logging.Logger;


public class BootstrapperRunner<ARGS_T extends StartArgs>
{
	private Logger logger = Logger.getLogger( this.getClass().getName() );

	private Bootstrapper app;
	private String[] args;
	private RunMode runMode = RunMode.DEBUG;


	public BootstrapperRunner( Bootstrapper app, String... args )
	{
		this.app = app;
		this.args = args;
	}

	public void setRunMode( RunMode runMode ) {
		this.runMode = runMode;
	}

	public void runNoCatch() throws Exception
	{
		StartMode startMode = StartMode.START;
		ExitCode exitCode = ExitCode.STOP;

		while ( true )  {

			app.init( startMode, args );
			startMode = StartMode.RESTART;

			app.run();

			app.dispose( exitCode );

			if ( exitCode == ExitCode.STOP ) {
				break;
			} else {
				logger.info( "Restarting" );
			}
		}
	}


	public void runCatch()
	{
		StartMode startMode = StartMode.START;
		ExitCode exitCode = ExitCode.STOP;

		try {

			while ( true )  {

				app.init( startMode, args );
				startMode = StartMode.RESTART;

				try {
					exitCode = app.run();
				} finally {
					app.dispose( exitCode );
				}

				if ( exitCode == ExitCode.STOP ) {
					break;
				} else {
					logger.info( "Restarting" );
				}
			}

		} catch ( CommandLineError exc ) {
			logger.warning( exc.getMessage() + " Exiting." );
		} catch ( Throwable tbl ) {
			StringWriter writer = new StringWriter();
			tbl.printStackTrace( new PrintWriter( writer ) );

			logger.severe( "The program has encountered an irrecoverable error and will exit.\n" +
							"Details:\n"
							+ writer );
        }
	}


	public void run() throws Exception
	{
		logger.info( "Starting" );

		switch ( runMode ) {
			case DEBUG:
				runNoCatch();
				break;
			case NORMAL:
				runCatch();
				break;
		}

		logger.info( "Exiting" );
	}

}


class CommandLineError extends Exception
{
    private static final long serialVersionUID = 1L;

	public CommandLineError( List<String> errors )
	{
		super( "Error parsing command line args. Illegal args: " + errors );
	}
}
