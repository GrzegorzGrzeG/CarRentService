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

    public RentalService(RentalRepository rentalRepository, CarService carService) {
        this.rentalRepository = rentalRepository;
        this.carService = carService;
    }

    public void addNewRental(Rental rental) {
        LocalDateTime start = rental.getStartDate();
        LocalDateTime end = rental.getEndDate();

        // 1. Czy auto dostępne w [start, end]?
        boolean available = isCarAvailableDuring(rental.getCarId(), start, end);
        if (!available) {
            throw new IllegalArgumentException("Auto w tym terminie jest już zarezerwowane!");
        }

        // 2. Obliczenie czasu trwania rezerwacji.
        //    W Duration mamy różnicę w sekundach / minutach / godzinach.
        Duration duration = Duration.between(start, end);
        rental.setDuration(duration);

        // 3. Obliczenie ceny (w oparciu o pełne dni z zaokrągleniem w górę).
        long fullDays = ChronoUnit.DAYS.between(start, end);

        // Sprawdzamy, czy jest jakaś "nadwyżka" ponad pełne dni
        // (np. wynajem od 10 stycznia 10:00 do 11 stycznia 09:59 to prawie 1 dzień,
        //  a od 10 stycznia 10:00 do 11 stycznia 10:01 to już 2 pełne dni do policzenia).
        LocalDateTime possibleEnd = start.plusDays(fullDays);
        if (possibleEnd.isBefore(end)) {
            fullDays++;
        }

        Car car = carService.getCarById(rental.getCarId());
        double dailyPrice = car.getPricePerDay();
        double totalPrice = fullDays * dailyPrice;
        rental.setPrice(totalPrice);

        // 4. Ustawienie statusu (np. od razu RENTED, albo najpierw RESERVED).
        rental.setStatus(RentalStatus.RENTED);

        // 5. Zapis do bazy.
        rentalRepository.save(rental);

        // 6. (Opcjonalnie) Ustawiamy auto jako niedostępne "od razu".
        //    Jeśli chcesz, żeby do czasu fizycznego rozpoczęcia
        //    też było "zajęte" w systemie.
        //    Jeśli wolisz, by było zablokowane dopiero w dniu startu –
        //    możesz pominąć tę linię lub wprowadzić inną logikę.
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
            carService.setCarAvailability(rental.getCarId(), true);
            carService.updateCar(car);
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

    public boolean isCarAvailableDuring(String carId,
                                        LocalDateTime start,
                                        LocalDateTime end) {
        // 1. Pobieramy wszystkie rezerwacje dla danego auta.
        List<Rental> rentalsForCar = rentalRepository.findAllByCarId(carId);

        // 2. Szukamy kolizji w datach z rezerwacjami, które nie mają statusu RETURNED.
        for (Rental r : rentalsForCar) {
            if (!r.getStatus().equals(RentalStatus.RETURNED)) {
                // Sprawdzamy, czy [start, end] nakłada się na [rentalStart, rentalEnd]
                if (datesOverlap(start, end, r.getStartDate(), r.getEndDate())) {
                    return false; // Kolizja => auto zajęte
                }
            }
        }
        return true; // Brak kolizji
    }

    private boolean datesOverlap(LocalDateTime start1,
                                 LocalDateTime end1,
                                 LocalDateTime start2,
                                 LocalDateTime end2) {
        return !start1.isAfter(end2) && !end1.isBefore(start2);
    }

}
