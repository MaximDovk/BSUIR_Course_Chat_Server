package me.shiftby.exception;

public class AlreadyExistException extends Exception {

    public AlreadyExistException(String message) {
        super(message);
    }
    public AlreadyExistException() {
    }
    public AlreadyExistException(Throwable cause) {
        super(cause);
    }
}
