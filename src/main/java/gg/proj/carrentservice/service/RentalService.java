package gg.proj.carrentservice.service;

import gg.proj.carrentservice.entity.Rental;
import gg.proj.carrentservice.entity.RentalStatus;
import gg.proj.carrentservice.entity.RentalView;
import gg.proj.carrentservice.repository.RentalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    //todo dodać obsługę zmiany statusów wypożyczeń
    public void addNewRental(Rental rental) {
        rental.setStatus(RentalStatus.RENTED);
        rentalRepository.save(rental);
        carService.setCarAvailability(rental.getCarId(), false);
    }

    public List<Rental> getAll() {
        return rentalRepository.findAll();
    }

    public List<RentalView> prepareRentalView(List<Rental> rentals) {
        //todo zaimplementować
        List<RentalView> toReturn = new ArrayList<>();
        for(Rental rental : rentals) {
            RentalView rentalView = new RentalView();
            rentalView.setRentalId(rental.getId());
            rentalView.setCarId(rental.getCarId());
            rentalView.setCustomerId(rental.getCustomerId());
            rentalView.setStartDate(rental.getStartDate());
            rentalView.setEndDate(rental.getEndDate());
            rentalView.setBrand(carService.getCarById(rental.getCarId()).getBrand());
            rentalView.setModel(carService.getCarById(rental.getCarId()).getModel());
            rentalView.setRegistrationNumber(carService.getCarById(rental.getCarId()).getRegistrationNumber());
            rentalView.setVin(carService.getCarById(rental.getCarId()).getVin());
            rentalView.setProductionYear(carService.getCarById(rental.getCarId()).getProductionYear());
            rentalView.setPower(carService.getCarById(rental.getCarId()).getPower());
            rentalView.setFuelType(carService.getCarById(rental.getCarId()).getFuelType());
            rentalView.setTransmission(carService.getCarById(rental.getCarId()).getTransmission());
            rentalView.setPricePerDay(carService.getCarById(rental.getCarId()).getPricePerDay());
            rentalView.setRentalStatus(rental.getStatus());
            toReturn.add(rentalView);
        }

        return toReturn;
    }

}
