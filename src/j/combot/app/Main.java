package j.combot.app;

import j.combot.app.App.RunMode;

public class Main
{
	public static void main( String[] args ) throws Exception
	{
		Bootstrapper<CombotArgs> bootstrapper = new Bootstrapper<CombotArgs>(
			new AppFactory<CombotArgs>() {
				public CombotApp make( CombotArgs args ) {
					return new CombotApp( args );
				}
			},
			new CombotArgs( args ) );

		bootstrapper.setRunMode( RunMode.DEBUG );

		bootstrapper.run();
	}
}
