package com.example.demo.customer2;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommandBus {

    private final ApplicationEventPublisher publisher;

    public void execute(Object command) {
        publisher.publishEvent(command);
    }
}
