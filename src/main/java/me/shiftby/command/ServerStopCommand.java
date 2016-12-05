package me.shiftby.command;

import me.shiftby.Main;
import me.shiftby.entity.User;

import java.util.regex.Pattern;

public class ServerStopCommand implements Command {

    public ServerStopCommand(User from, String command, Pattern pattern) {
    }

    @Override
    public void execute() throws Exception {
        Main.stop();
    }
}
