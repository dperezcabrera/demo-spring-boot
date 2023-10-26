package com.example.demo.customer2.searchbyname;

import com.example.demo.customer.dtos.CustomerDto;
import java.util.List;

public record SearchCustomerResponse(List<CustomerDto> response) {

}
