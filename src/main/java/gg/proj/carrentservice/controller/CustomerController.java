package gg.proj.carrentservice.controller;

import gg.proj.carrentservice.entity.Customer;
import gg.proj.carrentservice.entity.CustomerView;
import gg.proj.carrentservice.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private final CustomerService customerService;


    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("customers", customerService.getAllCustomerViews(customerService.getAllCustomers()));
        return "/html/customer_list";
    }

    @PostMapping("/list")
    public String processList(@RequestParam("customerId") String customerId) {
        return "redirect:/customer/details" + customerId;
    }

    @GetMapping("/details/{id}")
    @ResponseBody
    public CustomerView getCustomerDetails(@PathVariable String id) {
        return customerService.getCustomerView(customerService.getCustomerById(id));
    }

    @GetMapping("/add")
    public String addCustomerForm(Model model) {
        model.addAttribute("newCustomer", new Customer());
        return "/html/add_customer";
    }

    @PostMapping("/add")
    public String addCustomer(@ModelAttribute("newCustomer") Customer customer) {
        customerService.saveCustomer(customer);
        return "redirect:/customer/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable String id) {
        customerService.deleteCustomer(id);
        return "redirect:/customer/list";
    }

}
