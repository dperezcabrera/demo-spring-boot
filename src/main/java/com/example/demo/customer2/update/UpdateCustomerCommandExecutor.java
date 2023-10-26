package com.example.demo.customer2.update;

import com.example.demo.customer.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateCustomerCommandExecutor {

    private final CustomerRepository customerRepository;
    private final UpdateCustomerMapper mapper;

    @EventListener
    public void execute(UpdateCustomerCommand cmd) {
        var customer = customerRepository.getReferenceById(cmd.customerId());
        mapper.map(cmd.dto(), customer);
        customerRepository.save(customer);
    }
}
