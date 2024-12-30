package gg.proj.carrentservice.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RentalView {
    private String rentalId;
    private String carId;
    private String customerId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String brand;
    private String model;
    private String registrationNumber;
    private String vin;
    private String productionYear;
    private int power;
    private FuelType fuelType;
    private TransmissionType transmission;
    private double pricePerDay;
    private RentalStatus rentalStatus;
    private double price;
    private String duration;
}
