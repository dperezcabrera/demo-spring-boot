package com.example.demo.customer.services;

import com.example.demo.customer.dtos.CustomerDto;
import com.example.demo.customer.dtos.CustomerNameDto;
import com.example.demo.customer.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateCustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;

    @Transactional
    public void updateCustomer(long customerId, CustomerNameDto dto) {
        var customer = customerRepository.getReferenceById(customerId);
        mapper.map(dto, customer);
        customerRepository.save(customer);
    }
    
    @Transactional
    public void updateCustomer(CustomerDto dto) {
        updateCustomer(dto.id(), mapper.map(dto));
    }
}
