package me.shiftby.command;

import me.shiftby.Main;
import me.shiftby.Session;
import me.shiftby.SessionManager;
import me.shiftby.entity.User;

public class DisconnectCommand implements Command {
    private User user;

    public DisconnectCommand(User user) {
        this.user = user;
    }

    @Override
    public void execute() throws Exception {
        SessionManager sm = Main.getSessionManager();
        Session session = sm.getByUsername(user.getUsername());
        sm.stopSession(session);
    }
}
