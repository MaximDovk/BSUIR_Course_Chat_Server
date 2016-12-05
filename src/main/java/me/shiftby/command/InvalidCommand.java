package me.shiftby.command;

import me.shiftby.Main;
import me.shiftby.Session;
import me.shiftby.entity.User;

public class InvalidCommand implements Command {
    private User user;

    public InvalidCommand(User user) {
        this.user = user;
    }

    @Override
    public void execute() throws Exception {
        Session session = Main.getSessionManager().getByUsername(user.getUsername());
        session.send("status.command.invalid");
    }
}
