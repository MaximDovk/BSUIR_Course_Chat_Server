package me.shiftby.command;

import me.shiftby.Main;
import me.shiftby.entity.User;
import me.shiftby.orm.GroupManager;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

public class ShowGroupsCommand implements Command {

    private User from;

    public ShowGroupsCommand(User from, String command, Pattern pattern) {
        this.from = from;
    }

    @Override
    public void execute() throws IOException {
        Set<String> groups = GroupManager.getInstance().getGroups();
        Main
                .getSessionManager()
                .getByUsername(from.getUsername())
                .send(groups
                        .stream()
                        .reduce((s1, s2) -> s1 + ":" + s2)
                        .orElse(""));
    }
}
