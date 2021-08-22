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
        notNull(customerDto.getId(), "El identificador no puede ser nulo");
        notNull(customerDto.getName(), "El nombre no puede ser nulo");
        var customer = customerRepository.getOne(customerDto.getId());
        customerMapper.map(customerDto, customer);
        customerRepository.save(customer);
    }
}
