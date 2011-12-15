package j.combot.app;

/**
 * TODO:
 *
 * Refactor Visual hierarchy. BaseVisual should only be superclass for simple
 * single value args. A new BaseCompositeVisual should be introduced and be
 * superclass of Composites.
 *
 *
 *
 *
 */

import j.combot.app.Bootstrapper.ExitCode;
import j.combot.app.Bootstrapper.StartMode;
import j.combot.command.Command;
import j.combot.command.CommandFactory;
import j.combot.gui.CombotGui;
import j.combot.gui.CommandData;
import j.util.prefs.GatheredPrefenences;
import j.util.prefs.PrefUtil;
import j.util.process.LineProcessCallback;
import j.util.process.ProcessCallback;
import j.util.process.ProcessHandler;
import j.util.util.Pair;
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

	private ProcessHandler processHandler;

	private static final String COMMAND_CHILDS = "command_childs";

	private Preferences rootPrefs = Preferences.userNodeForPackage( getClass() ),
			childCmdPrefs = rootPrefs.node( COMMAND_CHILDS );

	private Command runningCmd;

	private ProcessCallback processCallback;

	public CombotApp( StartArgs args ) {
	}

	public static Collection<Command> loadCmdPrefs( Preferences prefs, Command cmd ) throws BackingStoreException
	{
//		Preferences cmdPrefs = prefs.node( cmd.getTitle() );
		// TODO:
		Preferences cmdPrefs = new GatheredPrefenences( prefs.node( cmd.getTitle() ) );

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
		// All top level commands
		for ( CommandData cmdData : cmds.getChildren() ) {
			// Derivied children with other default values
			for ( CommandData d : cmdData.getChildren() ) {
				// TODO:
				PrefUtil.save( new GatheredPrefenences( prefs.node( cmdData.cmd.getTitle() ) ), d.cmd );
//				PrefUtil.save( prefs.node( cmdData.cmd.getTitle() ), d.cmd );
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
//			comClasses = path.listFiles( Util.sufixFileFilter( "ls.class" ) );
		} catch ( URISyntaxException exc ) {
			logger.warning( "Error while reading commands: " + exc );
			return;
		}

		List<Command> cmds = new ArrayList<>();

		for ( File com : comClasses ) {
			String filename = com.getName();
			String className = filename.substring( 0, filename.length() - ".class".length() );
			cmds.add( loadCommand( className ) );
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

		gui.setActiveCommand( 0 );
	}


	/**
	 * Looks in the default package and loads the CommandFactory class with name
	 * className, returning the Command that the factory creates.
	 */
	private static Command loadCommand( String factoryClassName )
	{
			CommandFactory commandFactory;

			try {
				commandFactory = (CommandFactory) Class.forName( factoryClassName ).newInstance();
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

		gui = new CombotGui( this );

		gui.init();
		loadCmds();
	}

	public void makeNewCommand( Command parentCmd, Command baseCmd, String newTitle )
	{
		Command newCmd = baseCmd.clone();
		newCmd.setTitle( newTitle );
		newCmd.setDefaultFromVisual();

		gui.addChildCommand( parentCmd, newCmd );
	}

	public void startCmd( Command cmd )
	{
		logger.info( "Running command: " + cmd );

		try {
		    Pair<ProcessHandler, String> p = cmd.createProcessHander( processCallback );
		    processHandler = p.a;
//		processHandler.setCallback( processCallback );

		    runningCmd = cmd;
			processHandler.runAsync();
			gui.onCommandStarted( p.b );

		} catch ( IOException exc ) {
		    // TODO: Hack, constant in code:
			logger.warning( "IO error when running command: " + cmd + "\n" + exc );
			onCommandTerminated( -1, "Could not start command: " + exc );
		}
	}

	/**
	 * User want to stop a running command
	 */
	public void stopCommand()
	{
		logger.info( "Command stopped by user" );
		processHandler.destroy();
		gui.onCommandStopped();
	}

	private void onCommandTerminated( int code, String msg )
	{
		gui.onHasTerminated( runningCmd, code, msg );
		processHandler = null;
		runningCmd = null;
	}


	public boolean isCmdRunnig() {
		return runningCmd != null;
	}

	public Command getRunningCmd() {
		return runningCmd;
	}

	public void dispose( ExitCode exitCode ) {
		gui.dispose();
	}

	{
	    // Init listener for output from the command process
		processCallback = new LineProcessCallback() {

            @Override
			public void receiveStdout( final String line ) {
				gui.runSyncInGuiThread( new Runnable() {
					public void run() {
					    if ( runningCmd == null ) return;
						gui.receiveOutput( runningCmd, line + "\n" );
					}
				} );
			}

            @Override
			public void receiveStderr( final String line ) {
				gui.runSyncInGuiThread( new Runnable() {
					public void run() {
					    if ( runningCmd == null ) return;
						gui.receiveError( runningCmd, line + "\n" );
					}
				} );
			}

            @Override
			public void signalTerminated( final int code ) {
				gui.runSyncInGuiThread( new Runnable() {
					public void run() {
						onCommandTerminated( code, null );
					}
				} );
			}

            @Override
            public void handleError( Object err ) {
                if ( err instanceof Throwable ) {
                    logger.warning( "Error in process handler: " +
                            Util.exceptionToString( (Throwable) err ) );
                } else {
                    logger.warning( "Error in process handler: " + err.toString() );
                }
            }
		};

	}



}
