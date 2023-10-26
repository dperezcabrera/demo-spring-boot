package com.example.demo.customer2.getall;

import com.example.demo.customer.repositories.CustomerRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetAllCustomersCommandExecutor {

    private final CustomerRepository customerRepository;
    private final GetAllCustomersMapper mapper;

    @EventListener
    public void execute(GetAllCustomersCommand cmd) {
        var result = customerRepository.findAll().stream()
                .map(mapper::map)
                .collect(Collectors.toList());
        
        cmd.callback().complete(new GetAllCustomersResponse(result));
    }
}
