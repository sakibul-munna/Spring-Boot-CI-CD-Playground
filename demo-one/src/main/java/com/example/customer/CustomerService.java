package com.example.customer;

import com.example.exception.DuplicateResourceException;
import com.example.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomer() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer id) {
        return customerDao.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFound(
                        "Customer with id [%s] not found!".formatted(id)
                ));
    }

    public Customer addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        String email = customerRegistrationRequest.email();
        if (customerDao.existsCustomerWithEmail(email)) {
            throw new DuplicateResourceException("Email already taken!");
        }

        Customer customer = new Customer();
        customer.setName(customerRegistrationRequest.name());
        customer.setEmail(customerRegistrationRequest.email());

        try {
            return customerDao.insertCustomer(customer);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to insert customer: " + exception.getMessage());
        }
    }

    public void deleteCustomer(Integer customerId) {
        Optional<Customer> customer = customerDao.selectCustomerById(customerId);
        if (customer.isEmpty()) {
            throw new ResourceNotFound("Customer with id [%s] not found".formatted(customerId));
        } else {
            try {
                customerDao.deleteCustomerById(customerId);
            } catch (Exception e) {
                throw new RuntimeException("Could not delete customer: " + e.getMessage());
            }
        }
    }
}
