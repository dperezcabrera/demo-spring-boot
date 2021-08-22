package com.example.demo.customer.services;

import com.example.demo.customer.dtos.CustomerDto;
import com.example.demo.customer.repositories.CustomerRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetCustomerByIdService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Transactional(readOnly = true)
    public CustomerDto getCustomerById(@NonNull Long customerId) {
        var customer = customerRepository.getOne(customerId);
        return customerMapper.map(customer);
    }
}
