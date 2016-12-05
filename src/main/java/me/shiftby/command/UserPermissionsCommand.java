package me.shiftby.command;

import me.shiftby.Main;
import me.shiftby.entity.User;

import java.util.regex.Pattern;

public class UserPermissionsCommand implements Command {

    private User from;

    public UserPermissionsCommand(User from, String command, Pattern pattern) {
        this.from = from;
    }


    @Override
    public void execute() throws Exception {
        Main
                .getSessionManager()
                .getByUsername(from.getUsername())
                .send(from.getRole().toString());
    }
}
