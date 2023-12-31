package com.springbootcicdplayground.customer;

import com.springbootcicdplayground.exception.DuplicateResourceException;
import com.springbootcicdplayground.exception.RequestValidationException;
import com.springbootcicdplayground.exception.ResourceNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomer() {
        underTest.getAllCustomer();

        verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        Long id = 10L;
        Customer customer = new Customer(
                id, "Test", "test01@gmail.com", 23
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        Customer actual = underTest.getCustomer(id);

        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowWhenGetCustomerReturnsEmptyOptional() {
        Long id = 10L;

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage("Customer with id [%s] not found!".formatted(id));
    }

    @Test
    void addCustomer() {

        String email = "test01@gmail.com";

        when(customerDao.existsCustomerWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest("Alex", email, 19);

        underTest.addCustomer(request);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getEmail()).isEqualTo(email);
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void willThrowWhenEmailExistsWhileAddingCustomer() {
        String email = "test01@gmail.com";

        when(customerDao.existsCustomerWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest("Alex", email, 19);

        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken!");

        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void canDeleteCustomer() {
        Long id = 10L;
        Customer customer = new Customer(
                id, "Test", "test01@gmail.com", 23
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        underTest.deleteCustomer(id);

        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void willThrowWhileDeleteCustomerGetInvalidId() {
        Long id = 10L;

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.deleteCustomer(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage("Customer with id [%s] not found".formatted(id));

        verify(customerDao, never()).deleteCustomerById(id);
    }

    @Test
    void canUpdateAllCustomerProperties() {
        Long id = 10L;
        Customer customer = new Customer(
                id, "Test", "test01@gmail.com", 23
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String updatedEmail = "updated@gmail.com";

        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest("Alex Test", updatedEmail, 23);

        when(customerDao.existsCustomerWithEmail(updatedEmail)).thenReturn(false);

        underTest.updateCustomer(id, customerUpdateRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getName()).isEqualTo(customerUpdateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customerUpdateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customerUpdateRequest.age());
    }

    @Test
    void canUpdateOnlyCustomerName() {
        Long id = 10L;
        Customer customer = new Customer(
                id, "Test", "test01@gmail.com", 23
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest("Alex Test", null, null);

        underTest.updateCustomer(id, customerUpdateRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getName()).isEqualTo(customerUpdateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateOnlyCustomerAge() {
        Long id = 10L;
        Customer customer = new Customer(
                id, "Test", "test01@gmail.com", 23
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(null, null, 25);

        underTest.updateCustomer(id, customerUpdateRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customerUpdateRequest.age());
    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        Long id = 10L;
        Customer customer = new Customer(
                id, "Test", "test01@gmail.com", 23
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String updatedEmail = "updated@gmail.com";
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(null, updatedEmail, null);
        when(customerDao.existsCustomerWithEmail(updatedEmail)).thenReturn(false);

        underTest.updateCustomer(id, customerUpdateRequest);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customerUpdateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void willThrowWhenEmailExistsWhileUpdatingCustomerEmail() {
        Long id = 10L;
        Customer customer = new Customer(
                id, "Test", "test01@gmail.com", 23
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String updatedEmail = "updated@gmail.com";
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(null, updatedEmail, null);
        when(customerDao.existsCustomerWithEmail(updatedEmail)).thenReturn(true);

        assertThatThrownBy(() -> underTest.updateCustomer(id, customerUpdateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");

        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void willThrowWhenCustomerUpdateHasNoChanges() {
        Long id = 10L;
        Customer customer = new Customer(
                id, "Test", "test01@gmail.com", 23
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(null, null, null);

        assertThatThrownBy(() -> underTest.updateCustomer(id, customerUpdateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("No data changes found");

        verify(customerDao, never()).updateCustomer(any());
    }




}