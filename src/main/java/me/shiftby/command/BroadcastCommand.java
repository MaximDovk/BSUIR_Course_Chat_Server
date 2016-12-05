package me.shiftby.command;

import me.shiftby.Main;
import me.shiftby.entity.User;

import java.util.regex.Pattern;

public class BroadcastCommand implements Command {
    private User from;
    private String message;

    public BroadcastCommand(User from, String command, Pattern pattern) {
        this.from = from;
        message = pattern.matcher(command).group(1);
    }

    @Override
    public void execute() throws Exception {
        Main.getSessionManager().broadcast(message, from);
    }

}
