package me.shiftby.logger;

public class ConsoleLogger implements Logger {

    private int level;

    public ConsoleLogger(int level) {
        this.level = level;
    }

    public void lowLevel(String message) {
        if (level > 3) {
            System.out.print("LOW: ");
            System.out.println(message);
        }
    }

    public void info(String message) {
        if (level > 2) {
            System.out.print("INFO: ");
            System.out.println(message);
        }
    }

    public void warning(String message) {
        if (level > 1) {
            System.out.print("WARNING: ");
            System.out.println(message);
        }
    }

    public void error(String message) {
        if (level > 0) {
            System.out.print("ERROR: ");
            System.out.println(message);
        }
    }
}
