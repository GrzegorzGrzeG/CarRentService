package gg.proj.carrentservice.controller;

import gg.proj.carrentservice.entity.BodyType;
import gg.proj.carrentservice.entity.Car;
import gg.proj.carrentservice.entity.FuelType;
import gg.proj.carrentservice.entity.TransmissionType;
import gg.proj.carrentservice.service.CarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/car")
public class CarController {

    @Autowired
    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "Test";
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("cars", carService.getAll());
        return "/html/car_list";
    }

    @PostMapping("/list")
    public String processList(@RequestParam("carId") String carId) {
        return "redirect:/car/details/" + carId;
    }

    @GetMapping("/add")
    public String newCarForm(Model model) {
        model.addAttribute("newCar", new Car());
        model.addAttribute("fuelTypes", FuelType.values());
        model.addAttribute("bodyType", BodyType.values());
        model.addAttribute("transmission", TransmissionType.values());
        return "/html/add_car";
    }

    @PostMapping("/add")
    public String processNewCarForm(@ModelAttribute("newCar") Car car) {
        carService.addNewCar(car);
        return "/html/index";
    }

    @GetMapping("/details/{id}")
    @ResponseBody
    public Car getCarDetails(@PathVariable String id) {
        return carService.getCarById(id);
    }




}
