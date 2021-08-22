package com.example.demo.customer.services;

import com.example.demo.customer.CustomerMother;
import com.example.demo.customer.dtos.CustomerDto;
import com.example.demo.customer.entities.Customer;
import com.example.demo.customer.repositories.CustomerRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@Tag("unit-test")
@ExtendWith(MockitoExtension.class)
public class SearchCustomersByNameServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    private CustomerMapper customerMapper = Mappers.getMapper(CustomerMapper.class);

    private CustomerMother customerMother = new CustomerMother();

    private SearchCustomersByNameService instance;

    @BeforeEach
    void initEachTest() {
        instance = new SearchCustomersByNameService(customerRepository, customerMapper);
    }

    @Test
    public void testSearchCustomersByNameHappyCase() {
        // Given
        String name = "John";
        List<Customer> customers = Arrays.asList(customerMother.john(), customerMother.john2(), customerMother.john3());
        List<CustomerDto> expectedCustomers = Arrays.asList(customerMother.johnDto(), customerMother.john2Dto(), customerMother.john3Dto());
        given(customerRepository.findByName(name)).willReturn(customers);

        // When
        List<CustomerDto> result = instance.searchCustomersByName(name);

        // Then
        assertEquals(expectedCustomers, result);
    }

    @Test
    public void testSearchCustomersByNameOneHappyCase() {
        // Given
        String name = "John";
        List<Customer> customers = Arrays.asList(customerMother.john());
        List<CustomerDto> expectedCustomers = Arrays.asList(customerMother.johnDto());
        given(customerRepository.findByName(name)).willReturn(customers);

        // When
        List<CustomerDto> result = instance.searchCustomersByName(name);

        // Then
        assertEquals(expectedCustomers, result);
    }

    @Test
    public void testSearchCustomersByNameEmptyHappyCase() {
        // Given
        String name = "John";
        List<Customer> customers = Collections.emptyList();
        List<CustomerDto> expectedCustomers = Collections.emptyList();
        given(customerRepository.findByName(name)).willReturn(customers);

        // When
        List<CustomerDto> result = instance.searchCustomersByName(name);

        // Then
        assertEquals(expectedCustomers, result);
    }

    @Test
    public void testSearchCustomersByNameNullPointerException() {
        // Given
        String name = null;

        // When
        assertThrows(NullPointerException.class, () -> instance.searchCustomersByName(name));

        // Then
        then(customerRepository).shouldHaveNoInteractions();
    }
}
