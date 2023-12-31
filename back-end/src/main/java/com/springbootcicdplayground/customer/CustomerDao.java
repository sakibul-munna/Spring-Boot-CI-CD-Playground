package com.springbootcicdplayground.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Long id);
    Customer insertCustomer(Customer customer);
    boolean existsCustomerWithEmail(String email);
    void deleteCustomerById(Long id);
    void updateCustomer(Customer customer);
;}
