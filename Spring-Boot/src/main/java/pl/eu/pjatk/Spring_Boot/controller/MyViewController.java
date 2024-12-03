package pl.eu.pjatk.Spring_Boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.eu.pjatk.Spring_Boot.model.Car;
import pl.eu.pjatk.Spring_Boot.service.CarService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class MyViewController {
    private CarService carService;

    @Autowired
    public MyViewController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("view/all")
    public String viewAllCars(Model model) {
        List<Car> carList = (List<Car>) carService.getCars();
        model.addAttribute("carList", carList);
        return "viewAll";
    }

    @GetMapping("searchCar")
    public String searchForCar(Model model) {
        model.addAttribute("car", new Car());
        return "searchCar";
    }

    @PostMapping("displayCar")
    public String displayCar(@RequestParam Long id, Model model) {
        Car car = carService.getCar(id);
        model.addAttribute("car", car);
        return "displayCar";
    }

    @GetMapping("addForm")
    public String displayAddForm(Model model) {
        model.addAttribute("car", new Car());
        return "addForm";
    }

    @PostMapping("addForm")
    public String submitAddForm(@ModelAttribute Car car) {
        this.carService.addCar(car);
        return "redirect:/view/all";
    }

    @GetMapping("editForm")
    public String displayEditForm(Model model) {
        model.addAttribute("car", new Car());
        return "editForm";
    }

    @PostMapping("editForm")
    public String submitEditForm(@ModelAttribute Car car) {
        this.carService.updateCar(car.getId(), car);
        return "redirect:/view/all";
    }

    @GetMapping("deleteForm")
    public String displayDeleteForm(Model model) {
        model.addAttribute("car", new Car());
        return "deleteForm";
    }

    @PostMapping("deleteForm")
    public String submitDeleteForm(@ModelAttribute Car car) {
        this.carService.deleteCar(car.getId());
        return "redirect:/view/all";
    }

    @GetMapping("search/brand")
    public String findByBrand(Model model) {
        List<Car> carList = new ArrayList<>();
        model.addAttribute("carList", carList);
        return "searchBrand";
    }

    @PostMapping("displayCarsByBrand")
    public String displayCarByBrand(@RequestParam String brand, Model model) {
        List<Car> carList = carService.getCarByBrand(brand);
        model.addAttribute("carList", carList);
        return "displayCarsByBrand";
    }

    @GetMapping("search/color")
    public String findByColor(Model model) {
        List<Car> carList = new ArrayList<>();
        model.addAttribute("carList", carList);
        return "searchColor";
    }

    @PostMapping("displayCarsByColor")
    public String displayCarByColor(@RequestParam String color, Model model) {
        List<Car> carList = carService.getCarByColor(color);
        model.addAttribute("carList", carList);
        return "displayCarsByColor";
    }
}
