package me.shiftby.command;

import me.shiftby.Session;
import me.shiftby.SessionManager;
import me.shiftby.entity.Message;
import me.shiftby.entity.User;
import me.shiftby.orm.MessageManager;
import me.shiftby.orm.UserManager;

import java.io.IOException;

public class PersonalMessageCommand implements Command {

    private User from;
    private String to;

    private String message;

    public PersonalMessageCommand(String to, User from, String message) {
        this.to = to;
        this.from = from;
        this.message = message;
    }

    @Override
    public void execute() throws IOException {
        Session receiver = SessionManager.getInstance().getByUsername(to);
        if (receiver != null) {
            receiver.send(new StringBuilder()
                    .append("/pm ")
                    .append(from.getUsername())
                    .append(" ")
                    .append(message)
                    .toString());
        } else {
            User user = UserManager.getInstance().findByUsername(to);
            if (user != null) {
                MessageManager.getInstance().saveMessage(new Message(from, user, message));
            } else {
                SessionManager.getInstance().getByUsername(from.getUsername()).send("status.message.receiver.invalid");
            }
        }
    }
}
