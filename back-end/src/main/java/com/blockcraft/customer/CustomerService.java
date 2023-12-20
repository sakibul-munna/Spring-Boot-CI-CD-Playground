package com.blockcraft.customer;

import com.blockcraft.exception.DuplicateResourceException;
import com.blockcraft.exception.RequestValidationException;
import com.blockcraft.exception.ResourceNotFound;
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

    public Customer getCustomer(Long id) {
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
        customer.setAge(customerRegistrationRequest.age());

        try {
            return customerDao.insertCustomer(customer);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to insert customer: " + exception.getMessage());
        }
    }

    public void deleteCustomer(Long customerId) {
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

    public void updateCustomer(Long id, CustomerUpdateRequest customerUpdateRequest) {
        Customer customer = getCustomer(id);

        boolean changes = false;

        if (customerUpdateRequest.name() != null && !customerUpdateRequest.name().equals(customer.getName())) {
            customer.setName(customerUpdateRequest.name());
            changes = true;
        }

        if (customerUpdateRequest.email() != null && !customerUpdateRequest.email().equals(customer.getEmail())) {
            if (customerDao.existsCustomerWithEmail(customerUpdateRequest.email())) {
                throw new DuplicateResourceException(
                        "Email already taken"
                );
            }
            customer.setEmail(customerUpdateRequest.email());
            changes = true;
        }

        if (customerUpdateRequest.age() != null && !customerUpdateRequest.age().equals(customer.getAge())) {
            customer.setAge(customerUpdateRequest.age());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No data changes found");
        } else {
            try {
                customerDao.updateCustomer(customer);
            } catch (Exception exception) {
                throw new RuntimeException("Could not update customer: " + exception.getMessage());
            }
        }
    }
}
