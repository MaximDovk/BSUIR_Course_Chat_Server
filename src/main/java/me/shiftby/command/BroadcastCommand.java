package me.shiftby.command;

import me.shiftby.SessionManager;
import me.shiftby.entity.User;

public class BroadcastCommand implements Command {
    private User from;
    private String message;

    public BroadcastCommand(User from, String message) {
        this.from = from;
        this.message = message;
    }

    @Override
    public void execute() throws Exception {
        SessionManager.getInstance().broadcast(message, from);
    }

}
