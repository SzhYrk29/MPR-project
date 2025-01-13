package pl.edu.pjatk.exception;

public class CarAlreadyExistsException extends RuntimeException {
  public CarAlreadyExistsException() {
    super("Car already exists");
  }
}
