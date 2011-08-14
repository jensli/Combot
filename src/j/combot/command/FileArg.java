package j.combot.command;

import j.combot.gui.visuals.FileVisual;

import java.io.File;


public class FileArg extends CommandPart<File, FileArg> {

	public FileArg( String title, String name ) {
		super( title, name, new FileVisual() );
	}
}
