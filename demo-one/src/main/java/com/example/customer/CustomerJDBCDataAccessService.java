package com.example.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {
    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT id, name, email, age
                FROM customer
                """;

        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        var sql = """
                SELECT id, name, email, age
                FROM customer
                WHERE id = ?
                """;

        return jdbcTemplate.query(sql, customerRowMapper, id).stream().findFirst();
    }

    @Override
    public Customer insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer(name, email, age)
                VALUES (?, ?, ?)
                """;

        int result = jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAge());

        System.out.println("jdbcTemplate.update = " + result);

        return customer;
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        var sql = """
                SELECT count(id)
                FROM customer
                WHERE email = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public void deleteCustomerById(Long id) {
        var sql = """
                DELETE
                FROM customer
                WHERE id = ?
                """;

        int result = jdbcTemplate.update(sql, id);
        System.out.println("deleteCustomerById Result: " + result);
    }

    @Override
    public void updateCustomer(Customer customer) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder("UPDATE customer SET ");

        if (customer.getName() != null) {
            sql.append("name = ?, ");
            params.add(customer.getName());
        }

        if (customer.getEmail() != null) {
            sql.append("email = ?, ");
            params.add(customer.getEmail());
        }

        if (customer.getAge() != null) {
            sql.append("age = ?, ");
            params.add(customer.getAge());
        }

        int len = sql.length();
        sql.delete(len - 2, len);
        sql.append(" WHERE id = ?");
        params.add(customer.getId());

        int result = 0;
        try {
            result = jdbcTemplate.update(sql.toString(), params.toArray());
        } catch (Exception e) {
            throw new RuntimeException("Could not update customer: " + e.getMessage());
        }

        System.out.println("jdbcUpdateCustomer Result: " + result);
    }
}
