package Server.src.main.java.ServerCommands;

import Server.src.main.java.MainServer;

public class QuitCommand extends ServerCommand {
    public QuitCommand() {
        super("quit");
    }

    @Override
    public boolean execute() {
        for (Thread thread: MainServer.getThreadList()) {
            thread.interrupt();
        }
        return false;
    }
}
