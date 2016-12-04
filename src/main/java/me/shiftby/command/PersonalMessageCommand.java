package me.shiftby.command;

import me.shiftby.Session;
import me.shiftby.SessionManager;

import java.io.IOException;

public class PersonalMessageCommand implements Command {

    private String from;
    private String to;

    private Session fromSession;

    private String message;

    public PersonalMessageCommand(String to, Session fromSession, String message) {
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
                .send(from + ':' + message);
    }
}
