package gg.proj.carrentservice.service;

import gg.proj.carrentservice.entity.Customer;
import gg.proj.carrentservice.entity.CustomerView;
import gg.proj.carrentservice.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(String id) {
        return customerRepository.findById(id).orElse(null);
    }

    public CustomerView getCustomerView(Customer customer) {
        CustomerView customerView = new CustomerView();
        customerView.setCustomerId(customer.getId());
        customerView.setFirstName(customer.getFirstName());
        customerView.setLastName(customer.getLastName());
        customerView.setEmail(customer.getEmail());
        customerView.setPhoneNumber(customer.getPhoneNumber());
        customerView.setAddress(customer.getAddress());
        customerView.setZipCode(customer.getZipCode());
        customerView.setCity(customer.getCity());
        customerView.setCountry(customer.getCountry());
        customerView.setDrivingLicenseNumber(customer.getDrivingLicenseNumber());
        customerView.setDrivingLicenseIssueDate(customer.getDrivingLicenseIssueDate());
        customerView.setDrivingLicenseExpirationDate(customer.getDrivingLicenseExpirationDate());
        customerView.setPesel(customer.getPesel());
        customerView.setIdNumber(customer.getIdNumber());
        customerView.setDateOfBirth(customer.getDateOfBirth());

        return customerView;
    }

    public List<CustomerView> getAllCustomerViews(List<Customer> customers) {
        List<CustomerView> customerViews = new ArrayList<>();
        for (Customer customer : customers) {
            CustomerView customerView = new CustomerView();
            customerView.setCustomerId(customer.getId());
            customerView.setFirstName(customer.getFirstName());
            customerView.setLastName(customer.getLastName());
            customerView.setEmail(customer.getEmail());
            customerView.setPhoneNumber(customer.getPhoneNumber());
            customerView.setAddress(customer.getAddress());
            customerView.setZipCode(customer.getZipCode());
            customerView.setCity(customer.getCity());
            customerView.setCountry(customer.getCountry());
            customerView.setDrivingLicenseNumber(customer.getDrivingLicenseNumber());
            customerView.setDrivingLicenseIssueDate(customer.getDrivingLicenseIssueDate());
            customerView.setDrivingLicenseExpirationDate(customer.getDrivingLicenseExpirationDate());
            customerView.setPesel(customer.getPesel());
            customerView.setIdNumber(customer.getIdNumber());
            customerView.setDateOfBirth(customer.getDateOfBirth());
            customerViews.add(customerView);
        }
        return customerViews;
    }

    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public void deleteCustomer(String id) {
        customerRepository.deleteById(id);
    }


}
