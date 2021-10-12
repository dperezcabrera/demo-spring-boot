package com.example.demo.customer.services;

import com.example.demo.customer.dtos.CustomerDto;
import com.example.demo.customer.repositories.CustomerRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.util.Assert.notNull;

@Service
@RequiredArgsConstructor
public class UpdateCustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Transactional
    public void updateCustomer(@NonNull CustomerDto customerDto) {
        notNull(customerDto.id(), "El identificador no puede ser nulo");
        notNull(customerDto.name(), "El nombre no puede ser nulo");
        var customer = customerRepository.getById(customerDto.id());
        customerMapper.map(customerDto, customer);
        customerRepository.save(customer);
    }
}
