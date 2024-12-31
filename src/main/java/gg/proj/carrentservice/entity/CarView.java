package gg.proj.carrentservice.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CarView {
    private String id;
    private String brand;
    private String model;
    private String color;
    private String registrationNumber;
    private String vin;
    private String productionYear;
    private int engineCapacity;
    private int power;
    private FuelType fuelType;
    private BodyType bodyType;
    private TransmissionType transmission;
    private int numberOfSeats;
    private double pricePerDay;
    private boolean isAvailable;
    private LocalDate lastTechnicalReview;
    private LocalDate nextTechnicalReview;
    private LocalDate insuranceValidTo;
    private Long mileage;
}
