package me.shiftby.command;

import me.shiftby.Main;

public class ServerStopCommand implements Command {

    @Override
    public void execute() throws Exception {
        Main.stop();
    }
}
