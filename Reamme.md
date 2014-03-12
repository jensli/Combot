Combot
======

Combot is a system for generating GUI:s for command line program. It is developed to make command line utilities more usable for users who are only familiar with graphical point-and-click interface.

Accessing and and building the source code
------------------------------------------

To download the source code of the project, run the command:
`git clone --recursive <repository name>`

The `--recursive` flag clones also sub repositories on which this projects depends.

Instructions for building and running the project can be found in:
Combot/Combot/doc/download_build_and_run.txt


Command example
---------------

Combot takes command specifications in the form of Java classes. It uses the specifications them to generate dialogs for specifying the input that commands need, running them and presenting their output to the user.

An example of a generated dialog can be seen in the image below.

![Combot generated GUI](https://bitbucket.org/lii/combot/raw/master/Find_command.png)

This dialog is an interface for the most basic feature of the Unix `find` command. The location parameter is a file path and this creates a field in the dialog with a button that opens a file chooser dialog. There are also text arguments, number arguments and optional arguments.

The following image shows how Combot can give useful error messages, for example if the user gives a search path to a non-existing file.

![Combot generated GUI](https://bitbucket.org/lii/combot/raw/master/Find_errors.png)


### Command specification

Commands are specified by creating a `CommandFactory` class and make it available to Combot. The following code shows the specification of the `find` command that is showed above.
 
    public class find implements CommandFactory {
        public Command make() {
            return new Command( "Find command", "find",
                        new FileArg( "Location", "", "/", FileOrDir.DIR, FileVal.ERROR_NOT_EXIST ),
    
                        new StringArg( "Name", "-name", "*" ),
    
                        new OptArg( false,
                            new CompositeArg( "Limit depth",
                                new IntArg( "Search depth", "-maxdepth", 0, Integer.MAX_VALUE, 10 ) ) ),
                        new OptArg( false, new ExtraArg( "Extra arguments" ) ),
    
                        new AltArg( "Symbolic link treatment", 1, 10,
                                new ConstArg( "Never follow links", "-P" ),
                                new ConstArg( "Follow links", "-L" ),
                                new ConstArg( "Only follow links in args", "-H" ) )
            );
        }
    }

 