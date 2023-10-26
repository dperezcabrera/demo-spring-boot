package com.example.demo.customer2.getall;

import com.example.demo.customer.dtos.CustomerDto;
import java.util.List;

public record GetAllCustomersResponse(List<CustomerDto> response) {

}
