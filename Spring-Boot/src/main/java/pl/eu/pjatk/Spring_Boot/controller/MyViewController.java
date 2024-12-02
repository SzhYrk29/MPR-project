package pl.eu.pjatk.Spring_Boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.eu.pjatk.Spring_Boot.model.Car;
import pl.eu.pjatk.Spring_Boot.service.CarService;

import java.util.List;

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
}
