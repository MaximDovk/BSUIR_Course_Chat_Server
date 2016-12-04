package me.shiftby.command;


import me.shiftby.SessionManager;
import me.shiftby.entity.Group;
import me.shiftby.entity.User;
import me.shiftby.orm.GroupManager;

import java.io.IOException;

public class GroupDisconnectCommand implements Command {

    private User user;
    private String to;

    public GroupDisconnectCommand(User user, String to) {
        this.to = to;
        this.user = user;
    }

    @Override
    public void execute() throws IOException {
        Group group = GroupManager.getInstance().findByName(to);
        if (group != null) {
            group.removeUser(user);
        } else {
            SessionManager.getInstance().getByUsername(user.getUsername()).send("status.group.invalid");
        }
    }
}