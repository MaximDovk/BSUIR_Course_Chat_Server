package me.shiftby.command;

import me.shiftby.Main;
import me.shiftby.entity.Group;
import me.shiftby.entity.User;
import me.shiftby.orm.GroupManager;

import java.io.IOException;

public class GroupMessageCommand implements Command {

    private User user;
    private String to;

    private String message;

    public GroupMessageCommand(User user, String to, String message) {
        this.to = to;
        this.user = user;
        this.message = message;
    }

    @Override
    public void execute() throws IOException {
        Group group = GroupManager.getInstance().findByName(to);
        if (group != null) {
            group.send(new StringBuilder()
                    .append("/gm ")
                    .append(user.getUsername())
                    .append(" ")
                    .append(to)
                    .append(" ")
                    .append(message)
                    .toString());
        } else {
            Main
                    .getSessionManager()
                    .getByUsername(user.getUsername())
                    .send("status.group.invalid");
        }
    }
}
