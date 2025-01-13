package pl.edu.pjatk.exception;

public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException() {
        super("Car not found");
    }
}
