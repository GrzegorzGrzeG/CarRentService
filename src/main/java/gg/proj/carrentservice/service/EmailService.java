package gg.proj.carrentservice.service;

import gg.proj.carrentservice.entity.Car;
import gg.proj.carrentservice.entity.Customer;
import gg.proj.carrentservice.entity.Rental;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final CustomerService customerService;
    private final CarService carService;

    public EmailService(JavaMailSender mailSender, CustomerService customerService, CarService carService) {
        this.mailSender = mailSender;
        this.customerService = customerService;
        this.carService = carService;
    }

    private boolean sendEmail(String to, String subject, String body) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
            mimeMessageHelper.setText(body, true);
            mailSender.send(mimeMessage);
            mimeMessageHelper.setTo(to);
            return true;
        } catch (MessagingException e) {
            log.error("Error sending email to: " + to + "with subject: " + subject + " error message: " + e.getMessage());
            return false;
        }
    }

    public boolean sendConfirmationEmail(Rental rental) {
        Car car = carService.getCarById(rental.getCarId());
        Customer customer = customerService.getCustomerById(rental.getCustomerId());

        if (Objects.isNull(customer.getEmail())) {
            log.error("Customer with id " + rental.getCustomerId() + " does not have email address");
            return false;
        }

        String body = "<!DOCTYPE html> <html><head>" +
                "<title>Your reservation has been submitted</title>" +
                "</head><body>" +
                "<p>Hi," + customer.getFirstName() + "</p>" +
                "<p>Thank you for booking your car" + car.getBrand() +" " + car.getModel() + "</p>" +
                "<p>Start date: " + rental.getStartDate() + "</p>" +
                "<p>End date: " + rental.getEndDate() + "</p>" +
                "<p>Price: " + rental.getPrice() + "</p>" +
                "<p>Best regards,</p>" +
                "<p>Car Rent Service Team</p>" +
                "</body> </html>";


        String subject = "Confirmation of reservation on " + car.getBrand() + " " + car.getModel();
        return sendEmail(customer.getEmail(), subject, body);
    }

    public boolean sendReturnEmail(Rental rental) {
        Customer customer = customerService.getCustomerById(rental.getCustomerId());

        if (Objects.isNull(customer.getEmail())) {
            log.error("Customer with id " + rental.getCustomerId() + " does not have email address");
            return false;
        }

        String body = "<!DOCTYPE html> <html><head>" +
                "<title>Confirmation of the return of the car</title>" +
                "</head><body>" +
                "<p>Hi," + customer.getFirstName() + "</p>" +
                "<p>Condition: " + rental.getCondition().toString() + "</p>" +
                "<p>Distance: " + rental.getDistance() + "</p>" +
                "<p>Price: " + rental.getPrice() + "</p>" +
                "<p>Best regards,</p>" +
                "<p>Car Rent Service Team</p>" +
                "</body> </html>";


        String subject = "Confirmation of the return of the car";
        return sendEmail(customer.getEmail(), subject, body);
    }


}
