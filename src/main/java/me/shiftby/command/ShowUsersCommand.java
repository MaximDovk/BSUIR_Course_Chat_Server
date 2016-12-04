package me.shiftby.command;

import me.shiftby.SessionManager;
import me.shiftby.entity.User;

import java.io.IOException;
import java.util.Set;

public class ShowUsersCommand implements Command{

    private User user;

    public ShowUsersCommand(User user) {
        this.user = user;
    }

    @Override
    public void execute() throws IOException {
        Set<String> users = SessionManager.getInstance().getUsers();
        SessionManager
                .getInstance()
                .getByUsername(user.getUsername())
                .send(users
                        .stream()
                        .reduce((s1, s2) -> s1 + ":" + s2)
                        .orElse(""));
    }
}
