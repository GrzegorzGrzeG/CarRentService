package gg.proj.carrentservice.service;

import gg.proj.carrentservice.entity.Rental;
import gg.proj.carrentservice.entity.RentalStatus;
import gg.proj.carrentservice.entity.RentalView;
import gg.proj.carrentservice.repository.RentalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class RentalService {
    private final RentalRepository rentalRepository;
    private final CarService carService;

    public RentalService(RentalRepository rentalRepository, CarService carService) {
        this.rentalRepository = rentalRepository;
        this.carService = carService;
    }

    public void addNewRental(Rental rental) {
        Duration duration = Duration.between(rental.getStartDate(), rental.getEndDate());
        rental.setDuration(duration);

        long days = duration.toDays();
        long hours = duration.minusDays(days).toHours();
        long minutes = duration.minusDays(days).minusHours(hours).toMinutes();

        long fullDays = ChronoUnit.DAYS.between(rental.getStartDate(), rental.getEndDate());

        if (rental.getStartDate().plusDays(fullDays).isBefore(rental.getEndDate())) {
            fullDays += 1;
        }
        rental.setPrice(fullDays * carService.getCarById(rental.getCarId()).getPricePerDay());

        rental.setStatus(RentalStatus.RENTED);
        rentalRepository.save(rental);
        carService.setCarAvailability(rental.getCarId(), false);
    }

    public List<Rental> getAll() {
        return rentalRepository.findAll();
    }

    public List<RentalView> prepareRentalView(List<Rental> rentals) {
        List<RentalView> toReturn = new ArrayList<>();
        for (Rental rental : rentals) {
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
            rentalView.setPrice(rental.getPrice());
            rentalView.setDuration(durationFormat(rental.getDuration()));
            toReturn.add(rentalView);
        }

        return toReturn;
    }

    public void returnRental(String rentalId) {
        Rental rental = rentalRepository.findById(rentalId).orElseThrow();
        if (Objects.nonNull(rental)) {
            rental.setStatus(RentalStatus.RETURNED);
            rentalRepository.save(rental);
            carService.setCarAvailability(rental.getCarId(), true);
        }
    }

    public List<RentalView> getRentalsByCustomerId(String customerId) {
        List<Rental> rentals = rentalRepository.findAllByCustomerId(customerId);
        return prepareRentalView(rentals);
    }

    private String durationFormat(Duration duration) {
        long days = duration.toDays();
        long hours = duration.minusDays(days).toHours();
        long minutes = duration.minusDays(days).minusHours(hours).toMinutes();
        return String.format("%d Days, %d Hours, %d Minutes", days, hours, minutes);
    }

    public List<Rental> getRentalsByAvailability(RentalStatus status) {
        return rentalRepository.findAllByStatus(status);
    }

}
