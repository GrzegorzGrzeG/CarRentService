package gg.proj.carrentservice.service;

import gg.proj.carrentservice.entity.Rental;
import gg.proj.carrentservice.entity.RentalStatus;
import gg.proj.carrentservice.repository.RentalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RentalService {
    private final RentalRepository rentalRepository;
    private final CarService carService;

    public RentalService(RentalRepository rentalRepository, CarService carService) {
        this.rentalRepository = rentalRepository;
        this.carService = carService;
    }

//    public List<Rental> getAll() {
//        return rentalRepository.getAll();
//    }

    //todo dodać obsługę zmiany statusów wypożyczeń
    public void addNewRental(Rental rental) {
        rental.setStatus(RentalStatus.RENTED);
        rentalRepository.save(rental);
        carService.setCarAvailability(rental.getCarId(), false);
    }

    public List<Rental> getAll() {
        return rentalRepository.findAll();
    }

}
