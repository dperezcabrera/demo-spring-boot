package com.example.demo.customer2.update;

import com.example.demo.customer2.dtos.UpdateCustomerDto;

public record UpdateCustomerCommand(long customerId, UpdateCustomerDto dto) {

}
