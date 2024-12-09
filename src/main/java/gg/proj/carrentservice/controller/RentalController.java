package gg.proj.carrentservice.controller;

import gg.proj.carrentservice.entity.Rental;
import gg.proj.carrentservice.service.CarService;
import gg.proj.carrentservice.service.RentalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@Controller
@RequestMapping("/rental")
public class RentalController {
    @Autowired
    private final RentalService rentalService;
    private final CarService carService;

    public RentalController(RentalService rentalService, CarService carService) {
        this.rentalService = rentalService;
        this.carService = carService;
    }

//    @RequestMapping("/list")
//    public String rentalList(Model model) {
//        model.addAttribute("rentals", rentalService.getAll());
//        return "/html/rental_list";
//    }

    @GetMapping("/list")
    public String rentalList(Model model) {
        model.addAttribute("rentals", rentalService.getAll());
        return "/html/rental_list";
    }

    @GetMapping("/add")
    public String newRentalForm(Model model) {
        model.addAttribute("cars", carService.getCarsByAvailable(true));
        model.addAttribute("rental", new Rental());
        return "/html/add_rental";
    }
    @PostMapping("/add")
    public String processNewRentalForm(@ModelAttribute("rental") Rental rental) {
        log.error("rental: " + rental);
        rentalService.addNewRental(rental);
        return "redirect:/rental/list";
    }
}
