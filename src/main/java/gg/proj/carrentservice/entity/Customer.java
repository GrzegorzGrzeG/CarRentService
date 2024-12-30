package gg.proj.carrentservice.entity;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;
@Data
@Document(collection = "customers")
public class Customer {
    @Id
    private String id;
    @NotBlank(message = "First name cannot be empty")
    private String firstName;
    @NotBlank(message = "Last name cannot be empty")
    private String lastName;
    @Email
    private String email;
    @Pattern(regexp = "^[0-9]{9}$", message = "Phone number must have 9 digits")
    private String phoneNumber;
    private String address;
    private String zipCode;
    private String city;
    private String country;
    @NotBlank(message = "Driving license number cannot be empty")
    private String drivingLicenseNumber;
    @Past
    @NotNull(message = "Driving license issue date cannot be empty")
    private LocalDate drivingLicenseIssueDate;
    @Future
    @NotNull(message = "Driving license expiration date cannot be empty")
    private LocalDate drivingLicenseExpirationDate;
    @Pattern(regexp = "^[0-9]{11}$", message = "PESEL must have 11 digits")
    private String pesel;
    @NotBlank(message = "ID number cannot be empty")
    private String idNumber;
    @Past
    @NotNull(message = "Date of birth cannot be empty")
    private LocalDate dateOfBirth;
}
