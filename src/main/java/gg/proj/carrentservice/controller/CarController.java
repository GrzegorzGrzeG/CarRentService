package gg.proj.carrentservice.controller;

import gg.proj.carrentservice.entity.*;
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
        model.addAttribute("cars", carService.getAllCarViews(carService.getAll()));
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

    @GetMapping("/delete/{id}")
    public String deleteCar(@PathVariable String id) {
        carService.deleteCar(id);
        return "redirect:/car/list";
    }

    @GetMapping("/edit/{id}")
    public String editCarForm(@PathVariable String id, Model model) {
        model.addAttribute("car", carService.getCarById(id));
        model.addAttribute("fuelTypes", FuelType.values());
        model.addAttribute("bodyType", BodyType.values());
        model.addAttribute("transmission", TransmissionType.values());
        return "/html/edit_car";
    }

    @PostMapping("/update")
    public String processEditCarForm(@ModelAttribute("car") Car car) {
        log.error("car id is {}", car.getBrand());
        carService.updateCar(car);
        return "redirect:/car/list";
    }

}
