package j.combot.app;

public interface AppFactory<ARGS_T extends StartArgs> {
	public App make( ARGS_T args );
}
