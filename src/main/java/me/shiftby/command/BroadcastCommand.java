package me.shiftby.command;

import me.shiftby.Main;
import me.shiftby.entity.Role;
import me.shiftby.entity.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BroadcastCommand implements Command {
    private User from;
    private String message;

    public BroadcastCommand(User from, String command, Pattern pattern) {
        this.from = from;
        Matcher m = pattern.matcher(command);
        m.matches();
        message = m.group(1);
    }

    @Override
    public void execute() throws Exception {
        Main.getSessionManager().broadcast(message, from);
    }

    @Override
    public Role getRole() {
        return Role.ADMIN;
    }
}
