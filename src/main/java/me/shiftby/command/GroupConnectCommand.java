package me.shiftby.command;

import me.shiftby.Main;
import me.shiftby.entity.Group;
import me.shiftby.entity.User;
import me.shiftby.orm.GroupManager;

import java.io.IOException;

public class GroupConnectCommand implements Command {

    private User user;
    private String to;

    public GroupConnectCommand(User user, String to) {
        this.to = to;
        this.user = user;
    }

    @Override
    public void execute() throws IOException {
        Group group = GroupManager.getInstance().findByName(to);
        if (group != null) {
            group.addUser(user);
        } else {
            Main.getSessionManager().getByUsername(user.getUsername()).send("status.group.invalid");
        }
    }
}
