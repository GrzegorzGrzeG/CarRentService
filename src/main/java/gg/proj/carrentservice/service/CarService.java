package gg.proj.carrentservice.service;

import gg.proj.carrentservice.entity.Car;
import gg.proj.carrentservice.repository.CarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class CarService {
    private final CarRepository carRepository;
    private final RentalService rentalService;

    public CarService(CarRepository carRepository, @Lazy RentalService rentalService) {
        this.carRepository = carRepository;
        this.rentalService = rentalService;
    }

    public List<Car> getAll() {
        return carRepository.findAll();
    }

    public void addNewCar(Car car) {
        car.setAvailable(true);
        carRepository.save(car);
    }

    public Car getCarById(String id) {
        return carRepository.findById(id).orElse(null);
    }

    public List<Car> getCarsByAvailable(boolean available) {
        return carRepository.getCarsByIsAvailable(available);
    }

    public void setCarAvailability(String id, boolean available) {
        Car car = carRepository.findById(id).orElse(null);
        if(Objects.nonNull(car)) {
            car.setAvailable(available);
            carRepository.save(car);
        }
    }

    public void deleteCar(String id) {
        carRepository.deleteById(id);
    }

    public Car getMostExpensiveCar() {
        List<Car> cars = carRepository.findAll();
        Car mostExpensiveCar = cars.get(0);
        for (Car car : cars) {
            if (car.getPricePerDay() > mostExpensiveCar.getPricePerDay()) {
                mostExpensiveCar = car;
            }
        }
        return mostExpensiveCar;
    }

//    public Car getMostOftenRentedCar() {
//        List<Car> cars = carRepository.findAll();
//        Car mostOftenRentedCar = cars.get(0);
//        for (Car car : cars) {
//            if (rentalService.getAll().stream().filter(rental -> rental.getCarId().equals(car.getId())).count() >
//                    rentalService.getAll().stream().filter(rental -> rental.getCarId().equals(mostOftenRentedCar.getId())).count()) {
//                mostOftenRentedCar = car;
//            }
//        }
//        return mostOftenRentedCar;
//    }

}
