package gg.proj.carrentservice.service;

import gg.proj.carrentservice.entity.Car;
import gg.proj.carrentservice.repository.CarRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    private final static Log log = LogFactory.getLog(CarService.class);
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getAll() {
        return carRepository.findAll();
    }

    public void addNewCar(Car car) {
        carRepository.save(car);
    }

    public Car getCarById(String id) {
        return carRepository.findById(id).orElse(null);
    }
}
