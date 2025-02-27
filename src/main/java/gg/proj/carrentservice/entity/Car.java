package gg.proj.carrentservice.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "cars")
public class Car {
    public static final Long dailyMileageLimit = 250L;
    public static final Long pricePerOverLimit = 2L;
    @Id
    private String id;
    @NotBlank(message = "Brand cannot be empty")
    private String brand;
    @NotBlank(message = "Model cannot be empty")
    private String model;
    private String color;
    @NotBlank(message = "Registration number cannot be empty")
    private String registrationNumber;
    @NotBlank(message = "VIN cannot be empty")
    private String vin;
    @NotBlank(message = "Production year cannot be empty")
    private String productionYear;
    @Min(1)
    private int engineCapacity;
    @Min(1)
    private int power;
    @NotNull
    private FuelType fuelType;
    @NotNull
    private BodyType bodyType;
    @NotNull
    private TransmissionType transmission;
    @Min(2)
    private int numberOfSeats;
    @Min(1)
    private double pricePerDay;
    //private boolean isAvailable;
    private LocalDate lastTechnicalReview;
    private LocalDate nextTechnicalReview;
    private LocalDate insuranceValidTo;
    private Long mileage;

}
