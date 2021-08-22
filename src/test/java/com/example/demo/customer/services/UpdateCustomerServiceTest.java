package com.example.demo.customer.services;

import com.example.demo.customer.CustomerMother;
import com.example.demo.customer.dtos.CustomerDto;
import com.example.demo.customer.repositories.CustomerRepository;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@Tag("unit-test")
@ExtendWith(MockitoExtension.class)
public class UpdateCustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    private CustomerMapper customerMapper = Mappers.getMapper(CustomerMapper.class);

    private CustomerMother customerMother = new CustomerMother();

    private UpdateCustomerService instance;

    @BeforeEach
    void initEachTest() {
        instance = new UpdateCustomerService(customerRepository, customerMapper);
    }

    @Test
    public void testChangeNameCustomerHappyCase() {
        // Given
        var defaultNameCustomer = customerMother.defaultNameCustomer();
        var john = customerMother.johnDto();
        var expectedCustomer = customerMother.john();
        given(customerRepository.getOne(defaultNameCustomer.getId())).willReturn(defaultNameCustomer);

        // When
        instance.updateCustomer(john);

        // Then
        then(customerRepository).should().save(eq(expectedCustomer));
    }

    @Test
    public void testUpdateCustomerNotFoundException() {
        // Given
        var john = customerMother.johnDto();
        var expectedException = new EntityNotFoundException();
        given(customerRepository.getOne(john.getId())).willThrow(expectedException);

        // When
        var exception = assertThrows(EntityNotFoundException.class, () -> instance.updateCustomer(john));

        // Then
        then(customerRepository).should(times(0)).save(any());
        assertEquals(expectedException, exception);
    }

    @Test
    public void testUpdateCustomerNullPointerException() {
        // Given
        CustomerDto nullCustomer = null;

        // When
        assertThrows(NullPointerException.class, () -> instance.updateCustomer(nullCustomer));

        // Then
        then(customerRepository).should(times(0)).save(any());
    }

    @Test
    public void testUpdateCustomerIdNullPointerException() {
        // Given
        var expectedErrorMessage = "El identificador no puede ser nulo";
        var customerWithNoId = customerMother.customerWithNoId();

        // When
        var ex = assertThrows(IllegalArgumentException.class, () -> instance.updateCustomer(customerWithNoId));

        // Then
        then(customerRepository).should(times(0)).save(any());
        assertEquals(expectedErrorMessage, ex.getMessage());
    }

    @Test
    public void testUpdateCustomerNameNullPointerException() {
        // Given
        var expectedErrorMessage = "El nombre no puede ser nulo";
        var customerWithNoName = customerMother.customerWithNoName();

        // When
        var ex = assertThrows(IllegalArgumentException.class, () -> instance.updateCustomer(customerWithNoName));

        // Then
        then(customerRepository).should(times(0)).save(any());
        assertEquals(expectedErrorMessage, ex.getMessage());
    }
}
