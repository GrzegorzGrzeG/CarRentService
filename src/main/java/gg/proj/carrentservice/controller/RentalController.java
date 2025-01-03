package gg.proj.carrentservice.controller;

import gg.proj.carrentservice.entity.*;
import gg.proj.carrentservice.service.CarService;
import gg.proj.carrentservice.service.CustomerService;
import gg.proj.carrentservice.service.EmailService;
import gg.proj.carrentservice.service.RentalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Slf4j
@Controller
@RequestMapping("/rental")
public class RentalController {
    @Autowired
    private final RentalService rentalService;
    private final CarService carService;
    private final EmailService emailService;

    public RentalController(RentalService rentalService, CarService carService, EmailService emailService) {
        this.rentalService = rentalService;
        this.carService = carService;
        this.emailService = emailService;
    }

    @GetMapping("/list")
    public String rentalList(Model model) {
        model.addAttribute("rentals", rentalService.prepareRentalView(rentalService.getRentalsByAvailability(RentalStatus.RENTED)));
        model.addAttribute("condition", RentalReturnCondition.values());
        return "/html/rental_list";
    }

    @GetMapping("/add")
    public String newRentalForm(Model model) {
        model.addAttribute("cars",carService.getAllCarViews(carService.getCarsByAvailable(true)));
        model.addAttribute("rental", new Rental());
        return "/html/add_rental";
    }
//todo dodać obłsugę błędów dla innych endpointów i po stronie frontu wyświetlanie tych błędów
    @PostMapping("/add")
    public String processNewRentalForm(@ModelAttribute("rental") Rental rental, Model model) {
        try {
            rentalService.addNewRental(rental);
            emailService.sendConfirmationEmail(rental);
        } catch (IllegalArgumentException ex) {
            // Błąd (np. kolizja terminów)
            model.addAttribute("errorMessage", "Car is not available in this period");
            // Ponownie załaduj listę dostępnych samochodów, jeśli to potrzebne
            model.addAttribute("cars", carService.getAllCarViews(carService.getCarsByAvailable(true)));
            return "/html/add_rental";
        }
        return "redirect:/rental/list";
    }

    @PostMapping("/return")
    public String processReturnRentalForm(@RequestParam("rentalId") String rentalId,
                                          @RequestParam("condition") RentalReturnCondition condition,
                                          @RequestParam("mileage") Long mileage) {
        rentalService.returnRental(rentalId, condition, mileage);
        return "redirect:/rental/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteRental(@PathVariable String id) {
        rentalService.deleteRental(id);
        return "redirect:/rental/list";
    }

    @GetMapping("/archive")
    public String archiveRentals(Model model) {
        model.addAttribute("rentals", rentalService.prepareRentalView(rentalService.getRentalsByAvailability(RentalStatus.RETURNED)));
        return "/html/rental_list";
    }

    @GetMapping("/edit/{id}")
    public String editRentalForm(@PathVariable String id, Model model) {
        model.addAttribute("rental", rentalService.getRentalById(id));
        model.addAttribute("status", RentalStatus.values());
        model.addAttribute("condition", RentalReturnCondition.values());
        return "/html/edit_rental";
    }

    @PostMapping("/update")
    public String processEditRentalForm(@ModelAttribute("rental") Rental rental) {
        rentalService.updateRental(rental);
        return "redirect:/rental/list";
    }
}
