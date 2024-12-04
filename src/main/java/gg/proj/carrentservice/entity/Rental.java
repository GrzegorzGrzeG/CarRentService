package gg.proj.carrentservice.entity;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "rentals")
public class Rental {

    @Id
    private String id;
    @NotNull
    private String carId;
    @NotNull
    private String customerId;
    @NotNull(message = "Start date cannot be empty")
    private Date startDate;
    @Future(message = "End date must be in the future")
    @NotNull(message = "End date cannot be empty")
    private Date endDate;
    @NotNull
    private RentalStatus status;

}
