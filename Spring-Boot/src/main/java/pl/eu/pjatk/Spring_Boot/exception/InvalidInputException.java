package pl.eu.pjatk.Spring_Boot.exception;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException() {
        super("Invalid input");
    }
}
