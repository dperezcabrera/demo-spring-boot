package com.example.demo.customer2.searchbyname;

import com.example.demo.customer.dtos.CustomerDto;
import com.example.demo.customer.entities.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SearchCustomerMapper {

    CustomerDto map(Customer dto);

}
