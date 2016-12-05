package me.shiftby.command;

import me.shiftby.Main;
import me.shiftby.entity.Group;
import me.shiftby.entity.Role;
import me.shiftby.entity.User;
import me.shiftby.orm.GroupManager;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupChangeCommand implements Command {

    private User from;
    private String group;
    private String key;
    private String value;

    public GroupChangeCommand(User from, String command, Pattern pattern) {
        this.from = from;
        Matcher m = pattern.matcher(command);
        m.matches();
        group = m.group(1);
        key = m.group(2);
        value = m.group(3);
    }

    @Override
    public void execute() throws IOException {
        Group g = GroupManager.getInstance().findByName(group);
        if (g != null) {
            if (g.isCreator(from)) {
                switch (key) {

                }
                Main.getSessionManager().getByUsername(from.getUsername()).send("status.group.changed");
            } else {
                Main.getSessionManager().getByUsername(from.getUsername()).send("status.permission.invalid");
            }
        } else {
            Main.getSessionManager().getByUsername(from.getUsername()).send("status.group.invalid");
        }
    }

    @Override
    public Role getRole() {
        return Role.USER;
    }
}
