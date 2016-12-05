package me.shiftby.command;

import me.shiftby.Main;
import me.shiftby.entity.Role;
import me.shiftby.entity.User;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

public class ShowUsersCommand implements Command{

    private User from;

    public ShowUsersCommand(User from, String command, Pattern pattern) {
        this.from = from;
    }

    @Override
    public void execute() throws IOException {
        Set<String> users = Main.getSessionManager().getUsers();
        Main
                .getSessionManager()
                .getByUsername(from.getUsername())
                .send(users
                        .stream()
                        .reduce((s1, s2) -> s1 + ":" + s2)
                        .orElse("status.empty"));
    }

    @Override
    public Role getRole() {
        return Role.USER;
    }
}
