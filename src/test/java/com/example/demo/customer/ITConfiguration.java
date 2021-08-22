package com.example.demo.customer;

import com.example.demo.customer.services.CustomerMapper;
import com.example.demo.customer.services.UpdateCustomerService;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@TestConfiguration
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:testdb")
@Import(UpdateCustomerService.class)
public class ITConfiguration {

    @Bean
    public CustomerMapper customerMapper() {
        return Mappers.getMapper(CustomerMapper.class);
    }

    @Bean
    public CustomerMother customerMother() {
        return new CustomerMother();
    }
}
