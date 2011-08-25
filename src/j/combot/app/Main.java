package j.combot.app;

import j.combot.app.Bootstrapper.RunMode;

public class Main
{

	// Make this into a DefaultBootstrapper class
	public static final class CombotBootstrapper implements Bootstrapper
	{
		private CombotApp app;

		public CombotBootstrapper() {
		}

		@Override
		public void init( StartMode mode, String[] strArgs )
		{
			CombotArgs args = new CombotArgs( strArgs );
			args.run();
			app = new CombotApp( args );
			app.init( mode );
		}

		@Override
		public void dispose( ExitCode code ) {
			app.dispose( code );
			app = null;
		}

		@Override
		public ExitCode run() throws Exception {
			return app.run();
		}
	}

	public static void main( String[] args ) throws Exception
	{
		BootstrapperRunner<CombotArgs> bootstrapper
			= new BootstrapperRunner<>( new CombotBootstrapper() );

		bootstrapper.setRunMode( RunMode.DEBUG );

		bootstrapper.run();
	}
}
