package pl.edu.pjatk.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjatk.exception.InvalidInputException;
import pl.edu.pjatk.model.Car;
import pl.edu.pjatk.service.CarService;

import java.io.IOException;
import java.util.List;

@RestController
public class MyRestController {
    private CarService carService;

    @Autowired
    public MyRestController(CarService carService) {
        this.carService = carService;
    }

    private static final Logger logger = LoggerFactory.getLogger(MyRestController.class);

    @GetMapping("car/all") // <- endpoint (displays all cars)
    public ResponseEntity<Iterable<Car>> getAll() {
        logger.info("Endpoint \"get all\" was invoked.");
        return new ResponseEntity<>(this.carService.getCars(), HttpStatus.OK);
    }

    @GetMapping("car/{id}") // <- endpoint (gets a car by id)
    public ResponseEntity<Car> get(@PathVariable String id) {
        try {
            Long parsedId = Long.parseLong(id);
            logger.info("Endpoint \"get by id\" was invoked using GET http method with id \"{}\".", id);
            return new ResponseEntity<>(this.carService.getCar(parsedId), HttpStatus.OK);
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
            Long parsedId = Long.parseLong(id);
            if (parsedId <= 0)
                throw new InvalidInputException();
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
            Long parsedId = Long.parseLong(id);
            if (parsedId <= 0)
                throw new InvalidInputException();
            this.carService.updateCar(parsedId, car);
            logger.info("Endpoint \"update by id\" was invoked using GET http method with data: id \"{}\", brand=\"{}\", color=\"{}\".", car.getId(), car.getBrand(), car.getColor());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (NumberFormatException e) {
            throw new InvalidInputException();
        }

    }

    @GetMapping("brand/{brand}") // <- endpoint (gets a car by brand)
    public ResponseEntity<List<Car>> getByBrand(@PathVariable String brand) {
        logger.info("Endpoint \"get by brand\" was invoked using GET http method with brand \"{}\".", brand);
        return new ResponseEntity<>(this.carService.getCarByBrand(brand), HttpStatus.OK);
    }

    @GetMapping("color/{color}") // <- endpoint (gets a car by color)
    public ResponseEntity<List<Car>> getByColor(@PathVariable String color) {
        logger.info("Endpoint \"get by color\" was invoked using GET http method with color \"{}\".", color);
        return new ResponseEntity<>(this.carService.getCarByColor(color), HttpStatus.OK);
    }

    @GetMapping("pdf/{id}") // <- endpoint (gets pdf with info about car with given id)
    public ResponseEntity<Void> getPdf(@PathVariable String id, HttpServletResponse response) throws IOException {
        try {
            Long parsedId = Long.parseLong(id);
            this.carService.getPdf(parsedId, response);
            logger.info("Endpoint \"get pdf\" was invoked using GET http method.");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new InvalidInputException();
        }

    }
}
