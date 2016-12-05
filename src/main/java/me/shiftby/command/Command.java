package me.shiftby.command;

import me.shiftby.entity.Role;

public interface Command {

    void execute() throws Exception;

    Role getRole();

}
