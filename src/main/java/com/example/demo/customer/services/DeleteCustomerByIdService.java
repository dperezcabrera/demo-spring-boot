package com.example.demo.customer.services;

import com.example.demo.customer.repositories.CustomerRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteCustomerByIdService {

    private final CustomerRepository customerRepository;

    @Transactional
    public void deleteCustomerById(@NonNull Long customerId) {
        customerRepository.deleteById(customerId);
    }
}
