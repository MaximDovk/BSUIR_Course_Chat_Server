package me.shiftby.command;

import me.shiftby.Session;

public class InvalidCommand implements Command {
    private Session session;

    public InvalidCommand(Session session) {
        this.session = session;
    }

    @Override
    public void execute() throws Exception {
        session.send("Invalid command");
    }
}
