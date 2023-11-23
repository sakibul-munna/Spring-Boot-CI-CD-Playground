package com.example.customer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getAllCustomer();
    }

    @GetMapping("{id}")
    public Customer getCustomer(@PathVariable("id") Integer id) {
        return customerService.getCustomer(id);
    }

    @PostMapping
    public ResponseEntity<Void> registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        Customer savedCustomer = customerService.addCustomer(customerRegistrationRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCustomer.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("{id}")
    public void deleteCustomer(@PathVariable("id") Integer id) {
        customerService.deleteCustomer(id);
    }
}
