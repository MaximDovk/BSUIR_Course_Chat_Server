package me.shiftby.command;

import me.shiftby.Session;

public class InvalidArgumentsCommand implements Command {
    private Session session;

    public InvalidArgumentsCommand(Session session) {
        this.session = session;
    }

    @Override
    public void execute() throws Exception {
        session.send("Invalid arguments");
    }
}
