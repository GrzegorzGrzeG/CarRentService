package gg.proj.carrentservice.service;

import gg.proj.carrentservice.entity.*;
import gg.proj.carrentservice.repository.RentalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class RentalService {
    private final RentalRepository rentalRepository;
    private final CarService carService;
    private final EmailService emailService;

    public RentalService(RentalRepository rentalRepository, CarService carService, EmailService emailService) {
        this.rentalRepository = rentalRepository;
        this.carService = carService;
        this.emailService = emailService;
    }

    public void addNewRental(Rental rental) {
        log.info("Starting to add new rental: {}", rental);

        // Sprawdzenie dostępności
        boolean available = isCarAvailableDuring(rental.getCarId(), rental.getStartDate(), rental.getEndDate());
        log.info("Car availability check result: {}", available);

        if (!available) {
            log.warn("Car is not available for the requested period");
            throw new IllegalArgumentException("Car is not available in this period");
        }

        // Obliczanie czasu trwania
        Duration duration = Duration.between(rental.getStartDate(), rental.getEndDate());
        rental.setDuration(duration);
        log.info("Rental duration: {}", duration);

        // Obliczanie ceny
        long fullDays = ChronoUnit.DAYS.between(rental.getStartDate(), rental.getEndDate());
        LocalDateTime possibleEnd = rental.getStartDate().plusDays(fullDays);
        if (possibleEnd.isBefore(rental.getEndDate())) {
            fullDays++;
        }
        Car car = carService.getCarById(rental.getCarId());
        double dailyPrice = car.getPricePerDay();
        double totalPrice = fullDays * dailyPrice;
        rental.setPrice(totalPrice);
        log.info("Calculated price: {}", totalPrice);

        // Ustawianie statusu
        rental.setStatus(RentalStatus.RENTED);
        log.info("Rental status set to: {}", rental.getStatus());

        // Zapisywanie w bazie
        rentalRepository.save(rental);
        log.info("Rental saved with ID: {}", rental.getId());

        // Wysyłanie e-maila
        emailService.sendConfirmationEmail(rental);
        log.info("Confirmation email sent");
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

    public void returnRental(String rentalId, RentalReturnCondition condition, Long mileage) {
        Rental rental = rentalRepository.findById(rentalId).orElseThrow();
        if (Objects.nonNull(rental)) {
            rental.setCondition(condition);
            Car car = carService.getCarById(rental.getCarId());
            car.setMileage(mileage);
            Long days = rental.getDuration().toDays();
            Long mileageLimit = days * Car.dailyMileageLimit;

            if (mileageLimit < mileage) {
                Long overLimit = (mileage - mileageLimit) * Car.pricePerOverLimit;
                rental.setPrice(rental.getPrice() + overLimit);
            }

            rental.setStatus(RentalStatus.RETURNED);
            rentalRepository.save(rental);
            carService.updateCar(car);
            emailService.sendReturnEmail(rental);
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

    public void deleteRental(String id) {
        rentalRepository.deleteById(id);
    }

    public Rental getRentalById(String rentalId) {
        return rentalRepository.findById(rentalId).orElseThrow();
    }

    public boolean isCarAvailableDuring(String carId, LocalDateTime start, LocalDateTime end) {
        List<Rental> rentalsForCar = rentalRepository.findAllByCarId(carId);

        for (Rental r : rentalsForCar) {
            log.info("Checking rental: ID={} Start={} End={} Status={}",
                    r.getId(), r.getStartDate(), r.getEndDate(), r.getStatus());
            if (!r.getStatus().equals(RentalStatus.RETURNED)) {
                if (datesOverlap(start, end, r.getStartDate(), r.getEndDate())) {
                    log.warn("Collision detected with rental ID={} for car ID={}", r.getId(), carId);
                    return false; // Auto zajęte
                }
            }
        }
        log.info("Car ID={} is available for the period {} to {}", carId, start, end);
        return true; // Brak kolizji
    }


    private boolean datesOverlap(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        boolean overlap = !start1.isAfter(end2) && !end1.isBefore(start2);
        log.info("Dates overlap check: [{} - {}] with [{} - {}] => {}", start1, end1, start2, end2, overlap);
        return overlap;
    }


    public void updateRental(Rental rental) {
        rentalRepository.save(rental);
    }

}
