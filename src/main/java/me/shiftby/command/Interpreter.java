package me.shiftby.command;

import me.shiftby.entity.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interpreter {

    public Command interpret(String command, User sender) {
        if (command.startsWith("/pm")) {
            Pattern pattern = Pattern.compile("/pm\\s+(\\w+)\\s+(.*)");
            Matcher matcher = pattern.matcher(command);
            if (matcher.matches() && matcher.groupCount() == 2) {
                return new PersonalMessageCommand(sender, matcher.group(1), matcher.group(2));
            } else {
                return new InvalidArgumentsCommand(sender);
            }
        } else if (command.startsWith("/gm")) {
            Pattern pattern = Pattern.compile("/gm\\s+(\\w+)\\s+(.*)");
            Matcher matcher = pattern.matcher(command);
            if (matcher.matches() && matcher.groupCount() == 2) {
                return new GroupMessageCommand(sender, matcher.group(1), matcher.group(2));
            } else {
                return new InvalidArgumentsCommand(sender);
            }
        } else if (command.startsWith("/bm")) {
            Pattern pattern = Pattern.compile("/bm\\s+(.*)");
            Matcher matcher = pattern.matcher(command);
            if (matcher.matches() && matcher.groupCount() == 1) {
                return new BroadcastCommand(sender, matcher.group(1));
            } else {
                return new InvalidArgumentsCommand(sender);
            }
        } else if (command.startsWith("/gconnect")) {
            Pattern pattern = Pattern.compile("/gconnect\\s+(.*)");
            Matcher matcher = pattern.matcher(command);
            if (matcher.matches() && matcher.groupCount() == 1) {
                return new GroupConnectCommand(sender, matcher.group(1));
            } else {
                return new InvalidArgumentsCommand(sender);
            }
        } else if (command.startsWith("/gdisconnect")) {
            Pattern pattern = Pattern.compile("/gdisconnect\\s+(.*)");
            Matcher matcher = pattern.matcher(command);
            if (matcher.matches() && matcher.groupCount() == 1) {
                return new GroupDisconnectCommand(sender, matcher.group(1));
            } else {
                return new InvalidArgumentsCommand(sender);
            }
        } else if (command.equals("/disconnect")) {
            return new DisconnectCommand(sender);
        } else if (command.equals("/stop")) {
            return new ServerStopCommand();
        }
        return new InvalidCommand(sender);
    }

}
