package me.shiftby.command;

import me.shiftby.Main;
import me.shiftby.Session;
import me.shiftby.SessionManager;
import me.shiftby.entity.User;

import java.util.regex.Pattern;

public class DisconnectCommand implements Command {
    private User from;

    public DisconnectCommand(User from, String command, Pattern pattern) {
        this.from = from;
    }

    @Override
    public void execute() throws Exception {
        SessionManager sm = Main.getSessionManager();
        Session session = sm.getByUsername(from.getUsername());
        sm.stopSession(session);
    }
}
