package me.shiftby;

import me.shiftby.command.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interpreter {

    public Command interpret(String command, Session sender) {
        if (command.startsWith("/pm")) {
            Pattern pattern = Pattern.compile("/pm\\s+(\\w+)\\s+(.*)");
            Matcher matcher = pattern.matcher(command);
            if (matcher.matches() && matcher.groupCount() == 2) {
                return new PersonalMessageCommand(matcher.group(1), sender, matcher.group(2));
            } else {
                return new InvalidArgumentsCommand(sender);
            }
        } else if (command.startsWith("/gm")) {
            Pattern pattern = Pattern.compile("/gm\\s+(\\w+)\\s+(.*)");
            Matcher matcher = pattern.matcher(command);
            if (matcher.matches() && matcher.groupCount() == 2) {
                return new GroupMessageCommand(matcher.group(1), sender, matcher.group(2));
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
        } else if (command.equals("/disconnect")) {
            return new DisconnectCommand(sender);
        }
        return new InvalidCommand(sender);
    }

}
