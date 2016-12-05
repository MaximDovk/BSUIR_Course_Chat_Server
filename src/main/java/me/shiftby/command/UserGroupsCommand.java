package me.shiftby.command;

import me.shiftby.Main;
import me.shiftby.entity.User;
import me.shiftby.orm.UserManager;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserGroupsCommand implements Command {

    private User from;
    private String user;

    public UserGroupsCommand(User from, String command, Pattern pattern) {
        this.from = from;
        Matcher m = pattern.matcher(command);
        user = m.group(1);
    }

    @Override
    public void execute() throws IOException {
        User u = UserManager.getInstance().findByUsername(user);
        if (u != null) {
            Set<String> groups = u.getGroups();
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
                    .send("status.user.invalid");
        }
    }

}
