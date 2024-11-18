package pl.eu.pjatk.Spring_Boot.exception;

public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException() {
        super("Car not found");
    }
}
