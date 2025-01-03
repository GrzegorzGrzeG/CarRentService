package gg.proj.carrentservice.entity;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class CustomerView {
    private String customerId;
    private List<String> rentalsId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String zipCode;
    private String city;
    private String country;
    private String drivingLicenseNumber;
    private LocalDate drivingLicenseIssueDate;
    private LocalDate drivingLicenseExpirationDate;
    private String pesel;
    private String idNumber;
    private LocalDate dateOfBirth;
}
