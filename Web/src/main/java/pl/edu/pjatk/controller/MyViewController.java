package pl.edu.pjatk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.edu.pjatk.model.Car;
import pl.edu.pjatk.service.CarService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MyViewController {
    private CarService carService;

    @Autowired
    public MyViewController(CarService carService) {
        this.carService = carService;
    }

    private static final Logger logger = LoggerFactory.getLogger(MyViewController.class);

    @GetMapping("view/all") // <- tested
    public String viewAllCars(Model model) {
        List<Car> carList = (List<Car>) carService.getCars();
        model.addAttribute("carList", carList);
        return "viewAll";
    }

    @GetMapping("searchCar") // <- tested
    public String searchForCar(Model model) {
        model.addAttribute("car", new Car());
        return "searchCar";
    }

    @PostMapping("displayCar") // <- tested
    public String displayCar(@RequestParam Long id, Model model) {
        Car car = carService.getCarById(id);
        model.addAttribute("car", car);
        logger.info("Page \"search car\" was submitted with id \"{}\".", id);
        return "displayCar";
    }

    @GetMapping("addForm") // <- tested
    public String displayAddForm(Model model) {
        model.addAttribute("car", new Car());
        return "addForm";
    }

    @PostMapping("addForm") // <- tested
    public String submitAddForm(@ModelAttribute Car car) {
        this.carService.addCar(car);
        logger.info("Page \"add form\" was submitted with data: brand=\"{}\", color=\"{}\".", car.getBrand(), car.getColor());
        return "redirect:/view/all";
    }

    @GetMapping("editForm") // <- tested
    public String displayEditForm(Model model) {
        model.addAttribute("car", new Car());
        return "editForm";
    }

    @PostMapping("editForm") // <- tested
    public String submitEditForm(@ModelAttribute Car car) {
        this.carService.updateCar(car.getId(), car);
        logger.info("Page \"edit form\" was submitted with data: brand=\"{}\", color=\"{}\".", car.getBrand(), car.getColor());
        return "redirect:/view/all";
    }

    @GetMapping("deleteForm") // <- tested
    public String displayDeleteForm(Model model) {
        model.addAttribute("car", new Car());
        return "deleteForm";
    }

    @PostMapping("deleteForm") // <- tested
    public String submitDeleteForm(@ModelAttribute Car car) {
        this.carService.deleteCar(car.getId());
        logger.info("Page \"delete form\" was submitted with id \"{}\".", car.getId());
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
        logger.info("Page \"search by brand\" was submitted with brand \"{}\".", brand);
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
        logger.info("Page \"search by color\" was submitted with color \"{}\".", color);
        return "displayCarsByColor";
    }
}
