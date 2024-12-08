package gg.proj.carrentservice.repository;

import gg.proj.carrentservice.entity.Rental;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends MongoRepository<Rental, Long> {

}
