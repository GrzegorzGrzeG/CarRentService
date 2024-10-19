package gg.proj.carrentservice.repository;

import gg.proj.carrentservice.entity.Car;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends MongoRepository<Car, Long> {
}
