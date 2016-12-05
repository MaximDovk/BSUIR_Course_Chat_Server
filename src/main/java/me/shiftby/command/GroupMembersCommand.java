package me.shiftby.command;

import me.shiftby.Main;
import me.shiftby.entity.Group;
import me.shiftby.entity.User;
import me.shiftby.orm.GroupManager;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupMembersCommand implements Command {

    private User from;
    private String group;

    public GroupMembersCommand(User from, String command, Pattern pattern) {
        this.from = from;
        Matcher m = pattern.matcher(command);
        group = m.group(1);
    }

    @Override
    public void execute() throws IOException {
        Group g = GroupManager.getInstance().findByName(group);
        if (g != null) {
            Set<String> groups = g.getMembers();
            Main
                    .getSessionManager()
                    .getByUsername(from.getUsername())
                    .send(groups
                            .stream()
                            .reduce((s1, s2) -> s1 + ":" + s2)
                            .orElse("status.empty"));
        } else {
            Main
                    .getSessionManager()
                    .getByUsername(from.getUsername())
                    .send("status.group.invalid");
        }
    }

}
