package gg.proj.carrentservice.controller;

import gg.proj.carrentservice.entity.Car;
import gg.proj.carrentservice.service.CarService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/car")
public class CarController {
    private final static Log log = LogFactory.getLog(CarController.class);
    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("cars", carService.getAll());
        return "/html/car_list";
    }

    @GetMapping("/add")
    public String newCarForm(Model model) {
        return "/html/add_car";
    }

    @PostMapping("/add")
    public String processNewCarForm(@ModelAttribute("newCar") Car car, Principal principal) {
        carService.addNewCar(car);
        return "/html/index";
    }

    @GetMapping("/view/{id}")
    public String viewCarForm(@PathVariable String id, Model model) {
        Car car = carService.getCarById(id);
        model.addAttribute("car", car);
        return "/html/car_view";
    }

    @PostMapping("/view/{id}")
    public String processViewCarForm(@RequestParam String id, Car car) {
        carService.addNewCar(car);
        return "/html/index";
    }




}
