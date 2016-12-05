package me.shiftby.command;

import me.shiftby.Main;
import me.shiftby.Session;
import me.shiftby.entity.Message;
import me.shiftby.entity.User;
import me.shiftby.orm.MessageManager;
import me.shiftby.orm.UserManager;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonalMessageCommand implements Command {

    private User from;
    private String to;

    private String message;

    public PersonalMessageCommand(User from, String command, Pattern pattern) {
        this.from = from;
        Matcher m = pattern.matcher(command);
        to = m.group(1);
        message = m.group(2);
    }

    @Override
    public void execute() throws IOException {
        Session receiver = Main.getSessionManager().getByUsername(to);
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
                Main.getSessionManager().getByUsername(from.getUsername()).send("status.message.receiver.invalid");
            }
        }
    }
}
