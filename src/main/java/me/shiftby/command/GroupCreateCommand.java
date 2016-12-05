package me.shiftby.command;

import me.shiftby.Main;
import me.shiftby.entity.Group;
import me.shiftby.entity.User;
import me.shiftby.exception.AlreadyExistException;
import me.shiftby.orm.GroupManager;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupCreateCommand implements Command {

    private User from;
    private String group;

    public GroupCreateCommand(User from, String command, Pattern pattern) {
        this.from = from;
        Matcher m = pattern.matcher(command);
        m.matches();
        group = m.group(1);
    }

    @Override
    public void execute() throws IOException {
        Group g = new Group(group);
        try {
            GroupManager.getInstance().createGroup(g);
            g.addUser(from);
        } catch (AlreadyExistException e) {
            Main.getSessionManager().getByUsername(from.getUsername()).send("status.group.exist");
        }
    }

}
