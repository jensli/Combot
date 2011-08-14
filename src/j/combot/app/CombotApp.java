package j.combot.app;

import j.combot.command.Command;
import j.combot.command.CommandFactory;
import j.combot.test_command.Gui;
import j.util.functional.Action0;
import j.util.functional.Action1;
import j.util.process.ProcessCallback;
import j.util.process.ProcessHandler;
import j.util.util.StringUtil;
import j.util.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CombotApp implements App {

	private Logger logger = Util.makeLogger( this );

	private Gui gui;

	private List<Command> commands = new ArrayList<Command>();

	private ProcessHandler processHandler;

	private ProcessCallback processCallback = new ProcessCallback() {

		public void receiveOutput( String line ) {
			gui.receiveOutput( line );
		}

		public void receiveError( String line ) {
			gui.receiveError( line );
		}

		public void signalTerminated( int code ) {
			gui.signalTerminated( code );
		}
	};

	public CombotApp( StartArgs args ) {

	}

	private void loadCommands()
	{
		commands.add( makeCommand( "grep" ) );
		commands.add( makeCommand( "ls" ) );
		commands.add( makeCommand( "find" ) );
	}

	public Command makeCommand( String name )
	{
			CommandFactory commandFactory;

			try {
				commandFactory = (CommandFactory) Class.forName( name ).newInstance();
			} catch ( InstantiationException exc ) {
				exc.printStackTrace();
				throw new RuntimeException( exc );
			} catch ( IllegalAccessException exc ) {
				exc.printStackTrace();
				throw new RuntimeException( exc );
			} catch ( ClassNotFoundException exc ) {
				exc.printStackTrace();
				throw new RuntimeException( exc );
			}

			return commandFactory.make();
	}


	public ExitCode run()
	{
		logger.info( "Running" );
		gui.run();

		return ExitCode.STOP;
	}

	@Override
	public void init( StartMode mode )
	{
		logger.info( "Initializing" );
		gui = new Gui();

		loadCommands();

		gui.setActiveCommand( commands.get( 2 ) );

		gui.getStartCaller().addListener( new Action0() {
			public void run() { startCommand(); }
		});

		gui.getStopCaller().addListener( new Action0() {
			public void run() { stopCommand(); }
		});

		gui.init();
	}

	private void stopCommand()
	{
		logger.info( "Stopping command" );
		processHandler.destroy();
		processHandler = null;
		gui.onCommandStopped();
	}

	private void startCommand()
	{
		Command cmd = gui.getActiveCommand().get();
		logger.info( "Running command: " + cmd );

		List<String> args = cmd.getArgStrings();
		args.add( 0, cmd.getName() );

		processHandler = new ProcessHandler( new ProcessBuilder( args ), processCallback );

		processHandler.setErrHandler( new Action1<Object>() {
			public void run( Object arg ) {
				if ( arg instanceof Throwable ) {
					logger.warning( "Error in process handler: " +
							Util.exceptionToString( (Throwable) arg ) );
				} else {
					logger.warning( "Error in process handler: " + arg.toString() );
				}
			}
		} );


		try {
			processHandler.start();
			gui.onCommandStarted( StringUtil.join( args, " " ) );
		} catch ( IOException exc ) {
			logger.warning( "IO error when running command: " + cmd + "\n" + exc );
			processHandler.destroy();
			processHandler = null;
			gui.signalTerminated( -1 ); // TODO: Send sensible info
		}


	}


	@Override
	public void dispose( ExitCode exitCode ) {
		gui.dipose();
	}




}
