package pl.edu.pjatk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pjatk.exception.CarNotFoundException;
import pl.edu.pjatk.exception.EmptyInputException;
import pl.edu.pjatk.exception.InvalidInputException;
import pl.edu.pjatk.model.Car;
import pl.edu.pjatk.repository.CarRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    private final CarRepository repository;

    @Autowired
    public CarService(CarRepository repository) {
        this.repository = repository;
    }

    public List<Car> getCarByBrand (String brand) {
        List<Car> cars = this.repository.findByBrand(brand);
        if (cars.isEmpty()) {
            throw new CarNotFoundException();
        }
        return cars;
    }

    public List<Car> getCarByColor (String color) {
        List<Car> cars = this.repository.findByColor(color);
        if (cars.isEmpty()) {
            throw new CarNotFoundException();
        }
        return cars;
    }

    public List<Car> getCars() {
        return this.repository.findAll();
    }

    public Car getCarById(Long id) {
        Optional<Car> optionalCar = this.repository.findById(id);

        if (id <= 0)
            throw new InvalidInputException();
        else if (optionalCar.isEmpty())
            throw new CarNotFoundException();
        else {
            return optionalCar.get();
        }
    }

    public void addCar(Car car) {
        if (car.getBrand() == null || car.getColor() == null || car.getBrand().isEmpty() || car.getColor().isEmpty())
            throw new EmptyInputException();

        this.repository.save(car);
    }

    public void deleteCar(Long id) {
        boolean exists = this.repository.existsById(id);
        if (!exists)
            throw new CarNotFoundException();

        this.repository.deleteById(id);
    }

    public void updateCar(Long id, Car car) {
        Car existingCar = this.repository.findById(id).orElseThrow(CarNotFoundException::new);

        if (car.getBrand() == null || car.getBrand().isEmpty() || car.getColor() == null || car.getColor().isEmpty()) {
            throw new EmptyInputException();
        }

        existingCar.setBrand(car.getBrand());
        existingCar.setColor(car.getColor());

        existingCar.setIdentifier();

        this.repository.save(existingCar);
    }

}
