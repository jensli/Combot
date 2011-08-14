package j.combot.app;

import j.combot.app.App.ExitCode;
import j.combot.app.App.RunMode;
import j.combot.app.App.StartMode;
import j.util.util.Util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.logging.Logger;


public class Bootstrapper<ARGS_T extends StartArgs>
{
	private Logger logger = Util.makeLogger( this );

	private ARGS_T args;
	private AppFactory<ARGS_T> appFactory;

	private RunMode runMode = RunMode.DEBUG;


	public Bootstrapper( AppFactory<ARGS_T> appFactory, ARGS_T args )
	{
		this.appFactory = appFactory;
		this.args = args;
	}

	public void setRunMode( RunMode runMode ) {
		this.runMode = runMode;
	}

	public void runNoCatch() throws Exception
	{
		App app = null;

		StartMode startMode = StartMode.START;
		ExitCode exitCode = ExitCode.STOP;

		args.run();

		while ( true )  {

			app = appFactory.make( args );

			app.init( startMode );
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
		App app = null;

		StartMode startMode = StartMode.START;
		ExitCode exitCode = ExitCode.STOP;

		try {

			args.run();

			while ( true )  {

				app = appFactory.make( args );

				app.init( startMode );
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
