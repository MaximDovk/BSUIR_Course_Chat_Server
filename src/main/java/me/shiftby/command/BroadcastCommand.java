package me.shiftby.command;

import me.shiftby.Main;
import me.shiftby.Session;
import me.shiftby.SessionManager;

public class BroadcastCommand implements Command {
    private Session fromSession;
    private String message;

    public BroadcastCommand(Session fromSession, String message) {
        this.fromSession = fromSession;
        this.message = message;
    }

    @Override
    public void execute() throws Exception {
        SessionManager.getInstance().broadcast(message, fromSession);
        Main.getLogger().info(fromSession.getLogin() + ": " + message);
    }

}
