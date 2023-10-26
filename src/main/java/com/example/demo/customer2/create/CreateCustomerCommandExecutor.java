package com.example.demo.customer2.create;

import com.example.demo.customer.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateCustomerCommandExecutor {

    private final CustomerRepository customerRepository;
    private final CreateCustomerMapper mapper;

    @EventListener
    public void execute(CreateCustomerCommand cmd) {
        var customer = mapper.map(cmd.customerDto());
        customerRepository.save(customer);
    }
}
