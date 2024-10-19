package gg.proj.carrentservice.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cars")
public class Car {
    @Id
    private Long id;
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
    @NotBlank(message = "Fuel type cannot be empty")
    private String fuelType;
    @NotNull
    private BodyType bodyType;
    @NotNull
    private TransmissionType transmission;
    @Min(2)
    private int numberOfSeats;
    @Min(1)
    private double pricePerDay;

}
