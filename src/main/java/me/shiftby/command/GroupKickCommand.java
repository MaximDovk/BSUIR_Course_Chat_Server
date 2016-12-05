package me.shiftby.command;

import me.shiftby.Main;
import me.shiftby.entity.Group;
import me.shiftby.entity.Role;
import me.shiftby.entity.User;
import me.shiftby.orm.GroupManager;
import me.shiftby.orm.UserManager;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupKickCommand implements Command {
    private User from;
    private String group;
    private String user;

    public GroupKickCommand(User from, String command, Pattern pattern) {
        this.from = from;
        Matcher m = pattern.matcher(command);
        m.matches();
        group = m.group(1);
        user = m.group(2);
    }

    @Override
    public void execute() throws IOException {
        Group g = GroupManager.getInstance().findByName(group);
        User u = UserManager.getInstance().findByUsername(user);
        if (g != null) {
            if (u != null) {
                if (g.isCreator(from)) {
                    g.removeUser(u);
                    Main.getSessionManager().getByUsername(from.getUsername()).send("status.group.kicked");
                } else {
                    Main.getSessionManager().getByUsername(from.getUsername()).send("status.permission.invalid");
                }
            } else {
                Main.getSessionManager().getByUsername(from.getUsername()).send("status.user.invalid");
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
