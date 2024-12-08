package gg.proj.carrentservice.controller;

import gg.proj.carrentservice.service.RentalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/rental")
public class RentalController {
    @Autowired
    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

//    @RequestMapping("/list")
//    public String rentalList(Model model) {
//        model.addAttribute("rentals", rentalService.getAll());
//        return "/html/rental_list";
//    }
}
