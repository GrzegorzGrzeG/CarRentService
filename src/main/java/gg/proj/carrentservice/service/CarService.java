package gg.proj.carrentservice.service;

import gg.proj.carrentservice.entity.Car;
import gg.proj.carrentservice.repository.CarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
