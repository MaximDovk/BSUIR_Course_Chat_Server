package me.shiftby.command;

import javafx.util.Pair;
import me.shiftby.entity.User;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interpreter {

    private HashMap<Pattern, Class> commands;

    public Interpreter(Properties properties) {
        commands = parseProperties(properties);
    }

    private HashMap<Pattern, Class> parseProperties(Properties properties) {
        HashMap<String, String> p = new HashMap<>();
        for (String property : properties.stringPropertyNames()) {
            p.put(property, properties.getProperty(property));
        }
        HashMap<Pattern, Class> result = new HashMap<>();
        p
                .keySet()
                .stream()
                .filter(s -> s.startsWith("regexp."))
                .map(s -> s.substring(7))
                .filter(s -> p.get("class." + s) != null)
                .forEach(s -> {
                    try {
                        Class c = Class.forName(p.get("class." + s));
                        result.put(Pattern.compile(p.get("regexp." + s)), c);
                    } catch (ClassNotFoundException ignored) {}
                });
        return result;
    }


    public Command interpret(String command, User sender)
            throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Pair<Pattern, Class> commandPair = commands
                .keySet()
                .parallelStream()
                .filter(p -> {
                    Matcher m = p.matcher(command);
                    return m.matches();
                })
                .map(p -> new Pair<>(p, commands.get(p)))
                .findAny()
                .orElse(new Pair<>(Pattern.compile(""), InvalidCommand.class));

        return (Command) commandPair
                .getValue()
                .getConstructor(User.class, String.class, Pattern.class)
                .newInstance(sender, command, commandPair.getKey());
    }

}
