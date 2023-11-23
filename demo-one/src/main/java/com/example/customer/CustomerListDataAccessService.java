package com.example.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao{
    static List<Customer> customers = new ArrayList<>();

    static {
        Customer alex = new Customer(
                1,
                "Alex",
                "alex@gmail.com"
        );

        Customer jamila = new Customer(2, "Jamila" , "jamila@gmail.com");

        customers.add(alex);
        customers.add(jamila);
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
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
    public void deleteCustomerById(Integer id) {
        customers.stream().filter(customer -> customer.getId().equals(id)).findFirst().ifPresent(customers::remove);
    }
}
