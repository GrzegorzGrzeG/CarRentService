package gg.proj.carrentservice.service;

import gg.proj.carrentservice.entity.Rental;
import gg.proj.carrentservice.repository.RentalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RentalService {
    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

//    public List<Rental> getAll() {
//        return rentalRepository.getAll();
//    }

}
