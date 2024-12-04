package gg.proj.carrentservice.controller;

import gg.proj.carrentservice.service.RentalService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rental")
public class RentalController {
    private final static Log log = LogFactory.getLog(RentalController.class);
    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @RequestMapping("/list")
    public String rentalList(Model model) {
        model.addAttribute("rentals", rentalService.getAll());
        return "/html/rental_list";
    }
}
