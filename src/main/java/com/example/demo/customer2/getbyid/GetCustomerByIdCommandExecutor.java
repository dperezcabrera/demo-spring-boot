package com.example.demo.customer2.getbyid;

import com.example.demo.customer.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetCustomerByIdCommandExecutor {

    private final CustomerRepository customerRepository;
    private final GetCustomerByIdMapper mapper;

    @EventListener
    public void execute(GetCustomerByIdCommand cmd) {
        var customer = customerRepository.getReferenceById(cmd.customerId());
        var result = mapper.map(customer);
        cmd.callback().complete(new GetCustomerByIdResponse(result));
    }
}
