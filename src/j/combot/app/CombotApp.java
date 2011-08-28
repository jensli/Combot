package j.combot.app;

import j.combot.app.Bootstrapper.ExitCode;
import j.combot.app.Bootstrapper.StartMode;
import j.combot.command.Command;
import j.combot.command.CommandFactory;
import j.combot.gui.CombotGui;
import j.util.functional.Action1;
import j.util.process.ProcessCallback;
import j.util.process.ProcessHandler;
import j.util.util.StringUtil;
import j.util.util.Util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

public class CombotApp
{

	private Logger logger = Util.getClassLogger( this );

	private CombotGui gui;

	private CommandContainer commands = new CommandContainer();

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

	public void deleteCmd( Command cmd ) {
		commands.getCommands().remove( cmd );
	}

	private void loadCommands()
	{
		URL url = getClass().getProtectionDomain().getCodeSource().getLocation();

		try {
			File path = new File( url.toURI() );

			File[] comClasses = path.listFiles( new FilenameFilter() {
				public boolean accept( File dir, String name ) {
					return name.endsWith( ".class" );
				}
			} );

			for ( File com : comClasses ) {
				String filename = com.getName();
				String className = filename.substring( 0, filename.length() - ".class".length() );
				commands.addParent( makeCommand( className ) );
			}


		} catch ( URISyntaxException exc ) {
			logger.warning( "Error while reading commands: " + exc );
		}

//		commands.add( makeCommand( "grep" ) );
//		commands.add( makeCommand( "ls" ) );
//		commands.add( makeCommand( "find" ) );
	}

	/**
	 * Looks in the default package and loads the CommandFactory class with name
	 * className, returning the Command that the factory creates.
	 */
	private static Command makeCommand( String className )
	{
			CommandFactory commandFactory;

			try {
				commandFactory = (CommandFactory) Class.forName( className ).newInstance();
			} catch ( InstantiationException | IllegalAccessException | ClassNotFoundException exc ) {
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

	public void init( StartMode mode )
	{
		logger.info( "Initializing" );
		loadCommands();

		gui = new CombotGui( this );

//		gui.getStartCaller().addListener( new Action0() {
//			public void run() { startCommand(); }
//		});
//
//		gui.getStopCaller().addListener( new Action0() {
//			public void run() { stopCommand(); }
//		});

//		gui.getNewCommandCaller().addListener( new Action1<NewCommandEvent>() {
//			public void run( NewCommandEvent arg ) {
//				makeNewCommand( arg.parentComamnd, arg.baseCmd, arg.newTitle );
//			}
//		} );

		gui.init();
		gui.setCommadList( commands );
//		gui.setActiveCommand( commands.get( 2 ) );
	}

	public void makeNewCommand( Command parentCmd, Command baseCmd, String newTitle )
	{
		Command newCmd = baseCmd.clone();
		newCmd.setDefaultFromVisual();
		newCmd.setTitle( newTitle );

		commands.addChild( parentCmd, newCmd );
		gui.addChildCommand( parentCmd, newCmd );
	}

	public void stopCommand()
	{
		logger.info( "Stopping command" );
		processHandler.destroy();
		processHandler = null;
		gui.onCommandStopped();
	}

	public void startCmd()
	{
		Command cmd = gui.getActiveCmd();
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


	public void dispose( ExitCode exitCode ) {
		gui.dispose();
	}


}
