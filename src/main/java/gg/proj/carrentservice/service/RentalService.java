package gg.proj.carrentservice.service;

import gg.proj.carrentservice.entity.Rental;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalService {
    private final static Log log = LogFactory.getLog(RentalService.class);
    private final RentalService rentalService;

    public RentalService(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    public List<Rental> getAll() {
        return rentalService.getAll();
    }

}
