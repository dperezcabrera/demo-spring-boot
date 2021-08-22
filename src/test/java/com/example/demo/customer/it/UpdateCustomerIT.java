package com.example.demo.customer.it;

import com.example.demo.customer.ITConfiguration;
import com.example.demo.customer.CustomerAgent;
import com.example.demo.customer.CustomerMother;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("web-db")
@SpringBootTest
@Import(ITConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UpdateCustomerIT {

    private final CustomerAgent customerAgent;

    private final CustomerMother customerMother;

    @Test
    void testChangeNameCustomerHappyCase() throws Exception {
        // Given
        var expectedName = "John";
        var noNameCustomer = customerMother.defaultNameCustomerDto();
        var john = customerMother.johnDto();
        customerAgent.createCustomer(noNameCustomer);

        // When
        var result = customerAgent.updateCustomer(john);

        // Then
        result.andExpect(status().isOk());
        var customer = customerAgent.getCustomer(noNameCustomer.getId());
        assertEquals(expectedName, customer.getName());
    }

    @Test
    void testUpdateCustomerNotFoundException() throws Exception {
        // Given
        var notExistCustomer = customerMother.notExistCustomer();

        // When
        var result = customerAgent.updateCustomer(notExistCustomer);

        // Then
        result.andExpect(status().isInternalServerError());
    }
}
