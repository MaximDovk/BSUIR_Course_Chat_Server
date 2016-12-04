package me.shiftby.command;

import me.shiftby.Session;
import me.shiftby.SessionManager;

import java.io.IOException;

public class GroupMessageCommand implements Command {

    private String from;
    private String to;

    private Session fromSession;

    private String message;

    public GroupMessageCommand(String to, Session fromSession, String message) {
        this.to = to;
        this.fromSession = fromSession;
        from = fromSession.getLogin();
        this.message = message;
    }

    @Override
    public void execute() throws IOException {
        SessionManager
                .getInstance()
                .getByLogin(to)
                .send(message);
    }
}
