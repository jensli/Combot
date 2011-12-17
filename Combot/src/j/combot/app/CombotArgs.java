package j.combot.app;

public class CombotArgs implements StartArgs {

    private String[] args;
    public boolean loadTestCmds = true;

	public CombotArgs( String... args ) {
	    this.args = args;
	}

	@Override
	public void run()
	{
	    String a;

	    for ( int i = 0; i < args.length; i++ ) {
	        a = args[i];

	        if ( a.equals( "--load_test_cmds" ) || a.equals( "-t" ) ) {
	            loadTestCmds = true;
	            continue;
	        }
	    }
	}
}
