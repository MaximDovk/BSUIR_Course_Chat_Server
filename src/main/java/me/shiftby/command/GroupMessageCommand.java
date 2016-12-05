package me.shiftby.command;

import me.shiftby.Main;
import me.shiftby.entity.Group;
import me.shiftby.entity.Role;
import me.shiftby.entity.User;
import me.shiftby.orm.GroupManager;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupMessageCommand implements Command {

    private User from;
    private String group;

    private String message;

    public GroupMessageCommand(User from, String command, Pattern pattern) {
        this.from = from;
        Matcher m = pattern.matcher(command);
        m.matches();
        group = m.group(1);
        message = m.group(2);
    }

    @Override
    public void execute() throws IOException {
        Group g = GroupManager.getInstance().findByName(group);
        if (g != null) {
            g.send(new StringBuilder()
                    .append("/gm ")
                    .append(from.getUsername())
                    .append(" ")
                    .append(group)
                    .append(" ")
                    .append(message)
                    .toString());
        } else {
            Main
                    .getSessionManager()
                    .getByUsername(from.getUsername())
                    .send("status.group.invalid");
        }
    }

    @Override
    public Role getRole() {
        return Role.USER;
    }
}
