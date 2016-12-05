package me.shiftby.command;

import me.shiftby.Main;
import me.shiftby.Session;
import me.shiftby.entity.Role;
import me.shiftby.entity.User;

import java.util.regex.Pattern;

public class InvalidCommand implements Command {
    private User from;

    public InvalidCommand(User from, String command, Pattern pattern) {
        this.from = from;
    }

    public InvalidCommand(User from) {
        this.from = from;
    }

    @Override
    public void execute() throws Exception {
        Session session = Main.getSessionManager().getByUsername(from.getUsername());
        session.send("status.command.invalid");
    }

    @Override
    public Role getRole() {
        return Role.USER;
    }
}
