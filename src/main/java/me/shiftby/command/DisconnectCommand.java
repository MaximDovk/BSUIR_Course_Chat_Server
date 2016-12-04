package me.shiftby.command;

import me.shiftby.Session;
import me.shiftby.SessionManager;

public class DisconnectCommand implements Command {
    private Session session;

    public DisconnectCommand(Session session) {
        this.session = session;
    }

    @Override
    public void execute() throws Exception {
        SessionManager.getInstance().stopSession(session);
    }
}
