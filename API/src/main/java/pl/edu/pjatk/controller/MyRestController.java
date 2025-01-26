package pl.edu.pjatk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjatk.exception.CarNotFoundException;
import pl.edu.pjatk.exception.InvalidInputException;
import pl.edu.pjatk.model.Car;
import pl.edu.pjatk.service.CarService;

import java.util.List;

@RestController
public class MyRestController {
    private final CarService carService;

    @Autowired
    public MyRestController(CarService carService) {
        this.carService = carService;
    }

    private static final Logger logger = LoggerFactory.getLogger(MyRestController.class);

    @GetMapping("car/all") // <- endpoint (displays all cars)
    public ResponseEntity<Iterable<Car>> getAll() {
        logger.info("Endpoint \"getCarById all\" was invoked.");
        return new ResponseEntity<>(this.carService.getCars(), HttpStatus.OK);
    }

    @GetMapping("car/{id}") // <- endpoint (gets a car by id)
    public ResponseEntity<Car> getCarById(@PathVariable String id) {
        try {
            long parsedId = Long.parseLong(id);
            if (parsedId <= 0)
                throw new CarNotFoundException();
            logger.info("Endpoint \"getCarById by id\" was invoked using GET http method with id \"{}\".", id);
            return new ResponseEntity<>(this.carService.getCarById(parsedId), HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new InvalidInputException();
        }
    }

    @PostMapping("car") // <- endpoint (adds a new car)
    public ResponseEntity<Void> addCar(@RequestBody Car car) {
        logger.info("Endpoint \"add car\" was invoked using POST http method with data: brand=\"{}\", color=\"{}\".", car.getBrand(), car.getColor());
        this.carService.addCar(car);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("delete/{id}") // <- endpoint (deletes a car)
    public ResponseEntity<Void> deleteCar(@PathVariable String id) {
        try {
            long parsedId = Long.parseLong(id);
            if (parsedId <= 0)
                throw new CarNotFoundException();
            this.carService.deleteCar(parsedId);
            logger.info("Endpoint \"delete by id\" was invoked using DELETE http method with id \"{}\".", id);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (NumberFormatException e) {
            throw new InvalidInputException();
        }
    }

    @PutMapping("update/{id}") // <- endpoint (updates car's info)
    public ResponseEntity<Void> updateCar(@PathVariable String id, @RequestBody Car car) {
        try {
            long parsedId = Long.parseLong(id);
            if (parsedId <= 0)
                throw new CarNotFoundException();
            this.carService.updateCar(parsedId, car);
            logger.info("Endpoint \"update by id\" was invoked using GET http method with data: id \"{}\", brand=\"{}\", color=\"{}\".", car.getId(), car.getBrand(), car.getColor());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (NumberFormatException e) {
            throw new InvalidInputException();
        }

    }

    @GetMapping("brand/{brand}") // <- endpoint (gets a car by brand)
    public ResponseEntity<List<Car>> getByBrand(@PathVariable String brand) {
        logger.info("Endpoint \"getCarById by brand\" was invoked using GET http method with brand \"{}\".", brand);
        return new ResponseEntity<>(this.carService.getCarByBrand(brand), HttpStatus.OK);
    }

    @GetMapping("color/{color}") // <- endpoint (gets a car by color)
    public ResponseEntity<List<Car>> getByColor(@PathVariable String color) {
        logger.info("Endpoint \"getCarById by color\" was invoked using GET http method with color \"{}\".", color);
        return new ResponseEntity<>(this.carService.getCarByColor(color), HttpStatus.OK);
    }
}
