package me.shiftby.command;

import me.shiftby.Session;
import me.shiftby.SessionManager;
import me.shiftby.entity.User;

public class InvalidArgumentsCommand implements Command {
    private User user;

    public InvalidArgumentsCommand(User user) {
        this.user = user;
    }

    @Override
    public void execute() throws Exception {
        Session session = SessionManager.getInstance().getByUsername(user.getUsername());
        session.send("status.command.arguments.invalid");
    }
}
