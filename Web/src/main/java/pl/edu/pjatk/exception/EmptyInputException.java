package pl.edu.pjatk.exception;

public class EmptyInputException extends RuntimeException {
    public EmptyInputException() {
        super("Input is empty");
    }
}
