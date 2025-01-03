package gg.proj.carrentservice.service;

import gg.proj.carrentservice.entity.Car;
import gg.proj.carrentservice.entity.CarView;
import gg.proj.carrentservice.repository.CarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getAll() {
        return carRepository.findAll();
    }

    public void addNewCar(Car car) {
        //car.setAvailable(true);
        carRepository.save(car);
    }

    public Car getCarById(String id) {
        return carRepository.findById(id).orElse(null);
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

    public CarView getCarView(Car car) {
        CarView carView = new CarView();
        carView.setId(car.getId());
        carView.setBrand(car.getBrand());
        carView.setModel(car.getModel());
        carView.setColor(car.getColor());
        carView.setRegistrationNumber(car.getRegistrationNumber());
        carView.setVin(car.getVin());
        carView.setProductionYear(car.getProductionYear());
        carView.setEngineCapacity(car.getEngineCapacity());
        carView.setPower(car.getPower());
        carView.setFuelType(car.getFuelType());
        carView.setBodyType(car.getBodyType());
        carView.setTransmission(car.getTransmission());
        carView.setNumberOfSeats(car.getNumberOfSeats());
        carView.setPricePerDay(car.getPricePerDay());
        //carView.setAvailable(car.isAvailable());
        carView.setLastTechnicalReview(car.getLastTechnicalReview());
        carView.setNextTechnicalReview(car.getNextTechnicalReview());
        carView.setInsuranceValidTo(car.getInsuranceValidTo());

        return carView;
    }

    public List<CarView> getAllCarViews(List<Car> cars) {
        List<CarView> carViews = new ArrayList<>();
        for(Car car : cars) {
            CarView carView = new CarView();
            carView.setId(car.getId());
            carView.setBrand(car.getBrand());
            carView.setModel(car.getModel());
            carView.setColor(car.getColor());
            carView.setRegistrationNumber(car.getRegistrationNumber());
            carView.setVin(car.getVin());
            carView.setProductionYear(car.getProductionYear());
            carView.setEngineCapacity(car.getEngineCapacity());
            carView.setPower(car.getPower());
            carView.setFuelType(car.getFuelType());
            carView.setBodyType(car.getBodyType());
            carView.setTransmission(car.getTransmission());
            carView.setNumberOfSeats(car.getNumberOfSeats());
            carView.setPricePerDay(car.getPricePerDay());
            //carView.setAvailable(car.isAvailable());
            carView.setLastTechnicalReview(car.getLastTechnicalReview());
            carView.setNextTechnicalReview(car.getNextTechnicalReview());
            carView.setInsuranceValidTo(car.getInsuranceValidTo());
            carViews.add(carView);
        }
        return carViews;
    }

    public void updateCar(Car car) {
        carRepository.save(car);
    }

}
