package me.shiftby.command;

import me.shiftby.Main;
import me.shiftby.entity.Group;
import me.shiftby.entity.User;
import me.shiftby.orm.GroupManager;

import java.io.IOException;
import java.util.Set;

public class GroupMembersCommand implements Command {

    private User user;
    private String group;

    public GroupMembersCommand(User user, String group) {
        this.group = group;
        this.user = user;
    }

    @Override
    public void execute() throws IOException {
        Group g = GroupManager.getInstance().findByName(group);
        if (g != null) {
            Set<String> groups = g.getMembers();
            Main
                    .getSessionManager()
                    .getByUsername(user.getUsername())
                    .send(groups
                            .stream()
                            .reduce((s1, s2) -> s1 + ":" + s2)
                            .orElse("status.empty"));
        } else {
            Main
                    .getSessionManager()
                    .getByUsername(user.getUsername())
                    .send("status.group.invalid");
        }
    }

}
