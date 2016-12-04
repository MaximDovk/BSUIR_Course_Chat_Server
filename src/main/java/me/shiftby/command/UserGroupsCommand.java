package me.shiftby.command;

import me.shiftby.SessionManager;
import me.shiftby.entity.User;
import me.shiftby.orm.UserManager;

import java.io.IOException;
import java.util.Set;

public class UserGroupsCommand implements Command {

    private User from;
    private String user;

    public UserGroupsCommand(User from, String user) {
        this.from = from;
        this.user = user;
    }

    @Override
    public void execute() throws IOException {
        User u = UserManager.getInstance().findByUsername(user);
        if (u != null) {
            Set<String> groups = u.getGroups();
            SessionManager
                    .getInstance()
                    .getByUsername(from.getUsername())
                    .send(groups
                            .stream()
                            .reduce((s1, s2) -> s1 + ":" + s2)
                            .orElse("status.empty"));
        } else {
            SessionManager.getInstance().getByUsername(from.getUsername()).send("status.user.invalid");
        }
    }

}
