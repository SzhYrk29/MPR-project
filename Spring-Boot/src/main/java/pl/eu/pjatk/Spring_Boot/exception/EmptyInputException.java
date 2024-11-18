package pl.eu.pjatk.Spring_Boot.exception;

public class EmptyInputException extends RuntimeException {
    public EmptyInputException() {
        super("Input is empty");
    }
}
