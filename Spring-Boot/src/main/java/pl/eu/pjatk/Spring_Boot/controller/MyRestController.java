package pl.eu.pjatk.Spring_Boot.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.eu.pjatk.Spring_Boot.model.Car;
import pl.eu.pjatk.Spring_Boot.service.CarService;

import java.io.IOException;
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
    public ResponseEntity<Car> get(@PathVariable Long id) {
        return new ResponseEntity<>(this.carService.getCar(id), HttpStatus.OK);
    }

    @PostMapping("car") // <- endpoint (adds a new car)
    public ResponseEntity<Void> addCar(@RequestBody Car car) {
        this.carService.addCar(car);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("delete/{id}") // <- endpoint (deletes a car)
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        this.carService.deleteCar(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("update/{id}") // <- endpoint (updates car's info)
    public ResponseEntity<Void> updateCar(@PathVariable Long id, @RequestBody Car car) {
        this.carService.updateCar(id, car);
        return new ResponseEntity<>(HttpStatus.CREATED);
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
    public ResponseEntity<Void> getPdf(@PathVariable Long id, HttpServletResponse response) throws IOException {
        this.carService.getPdf(id, response);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}