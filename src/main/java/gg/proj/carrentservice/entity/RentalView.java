package gg.proj.carrentservice.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RentalView {
    private String rentalId;
    private String carId;
    private String customerId;
    private LocalDate startDate;
    private LocalDate endDate;
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
}
