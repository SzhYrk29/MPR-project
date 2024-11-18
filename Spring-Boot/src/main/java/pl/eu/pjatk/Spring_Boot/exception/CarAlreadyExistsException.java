package pl.eu.pjatk.Spring_Boot.exception;

public class CarAlreadyExistsException extends RuntimeException {
    public CarAlreadyExistsException() {
        super("Car already exists");
    }
}
