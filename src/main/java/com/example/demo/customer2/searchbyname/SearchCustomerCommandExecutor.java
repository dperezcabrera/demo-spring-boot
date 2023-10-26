package com.example.demo.customer2.searchbyname;

import com.example.demo.customer.repositories.CustomerRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchCustomerCommandExecutor {

    private final CustomerRepository customerRepository;
    private final SearchCustomerMapper mapper;

    @EventListener
    public void execute(SearchCustomerCommand cmd) {
        var result = customerRepository.findByName(cmd.searchParams().name()).stream()
                .map(mapper::map)
                .collect(Collectors.toList());
        
        cmd.callback().complete(new SearchCustomerResponse(result));
    }
}
