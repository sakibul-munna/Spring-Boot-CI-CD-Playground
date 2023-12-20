package com.example.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao {
    static List<Customer> customers = new ArrayList<>();

    static {
        Customer alex = new Customer(
                1L,
                "Alex",
                "alex@gmail.com",
                22
        );

        Customer jamila = new Customer(2L, "Jamila", "jamila@gmail.com", 25);

        customers.add(alex);
        customers.add(jamila);
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        return customers.stream().filter(customer -> customer.getId().equals(id)).findFirst();
    }

    @Override
    public Customer insertCustomer(Customer customer) {
        customers.add(customer);
        return customer;
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        return customers.stream().anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public void deleteCustomerById(Long id) {
        customers.stream().filter(customer -> customer.getId().equals(id)).findFirst().ifPresent(customers::remove);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customers.add(customer);
    }
}
