package j.combot.app;

import j.combot.app.Bootstrapper.ExitCode;
import j.combot.app.Bootstrapper.StartMode;
import j.combot.command.Command;
import j.combot.command.CommandFactory;
import j.combot.gui.CombotGui;
import j.combot.gui.CommandData;
import j.util.functional.Action1;
import j.util.prefs.PrefUtil;
import j.util.process.ProcessCallback;
import j.util.process.ProcessHandler;
import j.util.util.StringUtil;
import j.util.util.Util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


public class CombotApp
{

	private Logger logger = Util.getClassLogger( this );

	private CombotGui gui;

//	private CommandContainer commands = new CommandContainer();

	private ProcessHandler processHandler;

	private static final String COMMAND_CHILDS = "command_childs";

	private Preferences rootPrefs = Preferences.userNodeForPackage( getClass() ),
			childCmdPrefs = rootPrefs.node( COMMAND_CHILDS );


	private ProcessCallback processCallback;

	public CombotApp( StartArgs args ) {
	}

	public static Collection<Command> loadCmdPrefs( Preferences prefs, Command cmd ) throws BackingStoreException
	{
		Preferences cmdPrefs = prefs.node( cmd.getTitle() );

		List<Command> result = new ArrayList<>();

		for ( String childName : cmdPrefs.childrenNames() ) {

			Command childCmd = cmd.clone();
			childCmd.setTitle( childName );

			PrefUtil.load( cmdPrefs, childCmd );

			result.add( childCmd );
		}

		return result;
	}


	public void onQuit( CommandData cmds )
	{
		logger.info( "Quitting" );
		savePrefs( cmds );
	}

	private void savePrefs( CommandData cmds )
	{
		try {
			childCmdPrefs.removeNode();
			childCmdPrefs = rootPrefs.node( COMMAND_CHILDS );

			saveCmdsPrefs( childCmdPrefs, cmds );

			childCmdPrefs.flush();
			logger.info( "Preferances saved" );
		} catch ( BackingStoreException exc ) {
			logger.warning( "Error when saving preferances: " +
					Util.exceptionToString( exc ) );
		}
	}

	public static void saveCmdsPrefs( Preferences prefs, CommandData cmds ) throws BackingStoreException
	{
		for ( CommandData cmdData : cmds.getChildren() ) {
			for ( CommandData d : cmdData.getChildren() ) {
				Preferences cmdChildPrefs = prefs.node( cmdData.cmd.getTitle() );
				PrefUtil.save( cmdChildPrefs, d.cmd );
			}
		}
	}


	private void loadCmds()
	{
		URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
		File[] comClasses;

		try {
			File path = new File( url.toURI() );
			comClasses = path.listFiles( Util.sufixFileFilter( ".class" ) );
		} catch ( URISyntaxException exc ) {
			logger.warning( "Error while reading commands: " + exc );
			return;
		}

		List<Command> cmds = new ArrayList<>();

		for ( File com : comClasses ) {
			String filename = com.getName();
			String className = filename.substring( 0, filename.length() - ".class".length() );
			cmds.add( makeCommand( className ) );
		}

		for ( Command cmd : cmds ) {
			gui.addCommand( cmd );

			try {
				Collection<Command> childCmds = loadCmdPrefs( childCmdPrefs, cmd );

				for ( Command child : childCmds ) {
					gui.addChildCommand( cmd, child );
				}

			} catch ( BackingStoreException exc ) {
				logger.warning( "Error while reading default values: "
						+ Util.exceptionToString( exc ) );
			}
		}



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
//		try {
//			rootPrefs.removeNode();
//		} catch ( BackingStoreException exc ) {
//			// TODO Auto-generated catch block
//			exc.printStackTrace();
//			throw new RuntimeException( exc );
//		}

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
		loadCmds();
//		gui.setCommadList( commands );
//		gui.setActiveCommand( commands.get( 2 ) );
	}

	public void makeNewCommand( Command parentCmd, Command baseCmd, String newTitle )
	{
		Command newCmd = baseCmd.clone();
		newCmd.setTitle( newTitle );
		newCmd.setDefaultFromVisual();

		gui.addChildCommand( parentCmd, newCmd );
	}

	public void stopCommand()
	{
		logger.info( "Command forcefully stopped" );
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


	{
		processCallback = new ProcessCallback() {

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

	}

}
