package com.example.demo.customer.it;

import com.example.demo.customer.ITConfiguration;
import com.example.demo.customer.CustomerMother;
import com.example.demo.customer.repositories.CustomerRepository;
import com.example.demo.customer.services.UpdateCustomerService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("service-db")
@DataJpaTest(showSql = false, useDefaultFilters = false)
@Import(ITConfiguration.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UpdateCustomerDbIT {

    private final CustomerRepository customerRepository;
    private final UpdateCustomerService updateCustomerService;
    private final CustomerMother customerMother;

    @Test
    public void testChangeNameCustomerHappyCase() {
        // Given
        var defaultNameCustomer = customerMother.defaultNameCustomer();
        var john = customerMother.johnDto();
        var expectedCustomer = customerMother.john();
        customerRepository.save(defaultNameCustomer);

        // When
        updateCustomerService.updateCustomer(john);
        var result = customerRepository.getById(john.getId());

        // Then
        assertEquals(expectedCustomer, result);
    }
}
