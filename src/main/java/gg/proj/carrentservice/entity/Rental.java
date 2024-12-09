package gg.proj.carrentservice.entity;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

//todo zmienić LocalDate na LocalDateTime albo zaimplementować drugie pole z godziną
@Data
@Document(collection = "rentals")
public class Rental {

    @Id
    private String id;
    @NotNull
    private String carId;
    @NotNull
    private String customerId;
    @NotNull(message = "Start date cannot be empty")
    private LocalDate startDate;
    @Future(message = "End date must be in the future")
    @NotNull(message = "End date cannot be empty")
    private LocalDate endDate;
    @NotNull
    private RentalStatus status;


}
