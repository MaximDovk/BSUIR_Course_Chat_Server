package me.shiftby.command;

import me.shiftby.Session;
import me.shiftby.SessionManager;
import me.shiftby.entity.User;

public class InvalidCommand implements Command {
    private User user;

    public InvalidCommand(User user) {
        this.user = user;
    }

    @Override
    public void execute() throws Exception {
        Session session = SessionManager.getInstance().getByUsername(user.getUsername());
        session.send("status.command.invalid");
    }
}
