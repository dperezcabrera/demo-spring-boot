package com.example.demo.customer2.delete;

import com.example.demo.customer.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteCustomerByIdCommandExecutor {

    private final CustomerRepository customerRepository;

    @EventListener
    public void execute(DeleteCustomerByIdCommand cmd) {
        customerRepository.deleteById(cmd.customerId());
    }
}
