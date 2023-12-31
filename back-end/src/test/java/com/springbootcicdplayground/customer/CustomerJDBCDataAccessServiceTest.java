package com.springbootcicdplayground.customer;

import com.springbootcicdplayground.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20
        );
        underTest.insertCustomer(customer);

        List<Customer> actual = underTest.selectAllCustomers();

        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerByIdGotInvalidId() {
        Long id = -1L;

        var actual = underTest.selectCustomerById(id);

        assertThat(actual).isEmpty();
    }

    @Test
    void existsCustomerWithEmail() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        boolean actual = underTest.existsCustomerWithEmail(email);

        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerWithEmailReturnsFalseWhenDoesNotExist() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        boolean actual = underTest.existsCustomerWithEmail(email);

        assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomerById() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers().stream().filter(c ->
                c.getEmail().equals(email)
        ).map(Customer::getId).findFirst().orElseThrow();

        underTest.deleteCustomerById(id);

        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isEmpty();
    }

    @Test
    void updateNameOfTheCustomer() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers().stream().filter(c ->
                c.getEmail().equals(email)
        ).map(Customer::getId).findFirst().orElseThrow();

        var newName = "Test";

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(id);
        updatedCustomer.setName(newName);

        underTest.updateCustomer(updatedCustomer);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).hasValueSatisfying(customer1 -> {
            assertThat(customer1.getName()).isEqualTo(newName);
            assertThat(customer1.getEmail()).isEqualTo(customer.getEmail());
            assertThat(customer1.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateEmailOfTheCustomer() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers().stream().filter(c ->
                c.getEmail().equals(email)
        ).map(Customer::getId).findFirst().orElseThrow();

        var newEmail = "test01@gmail.com";

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(id);
        updatedCustomer.setEmail(newEmail);

        underTest.updateCustomer(updatedCustomer);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).hasValueSatisfying(customer1 -> {
            assertThat(customer1.getEmail()).isEqualTo(newEmail);
            assertThat(customer1.getName()).isEqualTo(customer.getName());
            assertThat(customer1.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateAgeOfTheCustomer() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers().stream().filter(c ->
                c.getEmail().equals(email)
        ).map(Customer::getId).findFirst().orElseThrow();

        var newAge = 21;

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(id);
        updatedCustomer.setAge(newAge);

        underTest.updateCustomer(updatedCustomer);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).hasValueSatisfying(customer1 -> {
            assertThat(customer1.getAge()).isEqualTo(newAge);
            assertThat(customer1.getName()).isEqualTo(customer.getName());
            assertThat(customer1.getEmail()).isEqualTo(customer.getEmail());
        });
    }

    @Test
    void updateAllPropertiesOfCustomer(){
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers().stream().filter(c ->
                c.getEmail().equals(email)
        ).map(Customer::getId).findFirst().orElseThrow();

        var newName = "New Name";
        var newEmail = "new_email-01@gmail.com";
        var newAge = 200;

        Customer updatedCustomer = new Customer(
                id,
                newName,
                newEmail,
                newAge
        );

        underTest.updateCustomer(updatedCustomer);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getName()).isEqualTo(newName);
            assertThat(c.getEmail()).isEqualTo(newEmail);
            assertThat(c.getAge()).isEqualTo(newAge);
        });
    }

    @Test
    void willNotUpdateWhenNothingToUpdate(){
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        Customer savedCustomer = underTest.selectAllCustomers().stream().filter(c ->
                c.getEmail().equals(email)
        ).findFirst().orElseThrow();

        underTest.updateCustomer(savedCustomer);

        Optional<Customer> actual = underTest.selectCustomerById(savedCustomer.getId());

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getName()).isEqualTo(savedCustomer.getName());
            assertThat(c.getEmail()).isEqualTo(savedCustomer.getEmail());
            assertThat(c.getAge()).isEqualTo(savedCustomer.getAge());
        });
    }
}