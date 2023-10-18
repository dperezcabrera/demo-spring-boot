package com.example.demo.customer.it;

import com.example.demo.customer.CustomerAgent;
import com.example.demo.customer.CustomerMother;
import com.example.demo.customer.ITConfiguration;
import java.util.Arrays;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("web-db")
@SpringBootTest
@Import(ITConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SearchCustomersByNameIT {

    private final CustomerMother customerMother = new CustomerMother();

    private final CustomerAgent customerAgent;

    @Test
    public void testSearchCustomersByNameHappyCase() throws Exception {
        // Given
        var name = "John";
        var expectedResult = Arrays.asList(customerMother.johnDto(), customerMother.john2Dto(), customerMother.john3Dto());
        expectedResult.forEach(u -> customerAgent.createCustomer(u));

        // When
        var result = customerAgent.searchCustomersByName(name);

        // Then
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSearchCustomersByNameOneHappyCase() throws Exception {
        // Given
        var name = "Peter";
        var expectedResult = Arrays.asList(customerMother.peterDto());
        customerAgent.createCustomer(customerMother.peterDto());

        // When
        var result = customerAgent.searchCustomersByName(name);

        // Then
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSearchCustomersByNameEmptyHappyCase() throws Exception {
        // Given
        var name = "None";
        var expectedResult = Collections.emptyList();

        // When
        var result = customerAgent.searchCustomersByName(name);

        // Then
        assertEquals(expectedResult, result);
    }
}
