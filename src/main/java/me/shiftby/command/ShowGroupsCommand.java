package me.shiftby.command;

import me.shiftby.SessionManager;
import me.shiftby.entity.User;
import me.shiftby.orm.GroupManager;

import java.io.IOException;
import java.util.Set;

public class ShowGroupsCommand implements Command {

    private User user;

    public ShowGroupsCommand(User user) {
        this.user = user;
    }

    @Override
    public void execute() throws IOException {
        Set<String> groups = GroupManager.getInstance().getGroups();
        SessionManager
                .getInstance()
                .getByUsername(user.getUsername())
                .send(groups
                        .stream()
                        .reduce((s1, s2) -> s1 + ":" + s2)
                        .orElse(""));
    }
}
