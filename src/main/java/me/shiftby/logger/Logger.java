package me.shiftby.logger;

public interface Logger {
    void lowLevel(String message);
    void info(String message);
    void warning(String message);
    void error(String message);
}
