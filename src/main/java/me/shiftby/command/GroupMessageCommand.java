package me.shiftby.command;

import me.shiftby.entity.User;

import java.io.IOException;

public class GroupMessageCommand implements Command {

    private User from;
    private String to;

    private String message;

    public GroupMessageCommand(String to, User from, String message) {
        this.to = to;
        this.from = from;
        this.message = message;
    }

    @Override
    public void execute() throws IOException {
    }
}
