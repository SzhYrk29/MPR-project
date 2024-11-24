package pl.eu.pjatk.Spring_Boot.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.eu.pjatk.Spring_Boot.exception.InvalidInputException;
import pl.eu.pjatk.Spring_Boot.model.Car;
import pl.eu.pjatk.Spring_Boot.service.CarService;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

@RestController
public class MyRestController {
    private CarService carService;

    @Autowired
    public MyRestController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("car/all") // <- endpoint (displays all cars)
    public ResponseEntity<Iterable<Car>> getAll() {
        return new ResponseEntity<>(this.carService.getCars(), HttpStatus.OK);
    }

    @GetMapping("car/{id}") // <- endpoint (gets a car by id)
    public ResponseEntity<Car> get(@PathVariable String id) {
        try {
            Long parsedId = Long.parseLong(id);
            return new ResponseEntity<>(this.carService.getCar(parsedId), HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new InvalidInputException();
        }
    }

    @PostMapping("car") // <- endpoint (adds a new car)
    public ResponseEntity<Void> addCar(@RequestBody Car car) {
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
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (NumberFormatException e) {
            throw new InvalidInputException();
        }

    }

    @GetMapping("brand/{brand}") // <- endpoint (gets a car by brand)
    public ResponseEntity<List<Car>> getByBrand(@PathVariable String brand) {
        return new ResponseEntity<>(this.carService.getCarByBrand(brand), HttpStatus.OK);
    }

    @GetMapping("color/{color}") // <- endpoint (gets a car by color)
    public ResponseEntity<List<Car>> getByColor(@PathVariable String color) {
        return new ResponseEntity<>(this.carService.getCarByColor(color), HttpStatus.OK);
    }

    @GetMapping("pdf/{id}") // <- endpoint (gets pdf with info about car with given id)
    public ResponseEntity<Void> getPdf(@PathVariable String id, HttpServletResponse response) throws IOException {
        try {
            Long parsedId = Long.parseLong(id);
            this.carService.getPdf(parsedId, response);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new InvalidInputException();
        }

    }
}