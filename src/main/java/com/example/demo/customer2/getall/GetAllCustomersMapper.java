package com.example.demo.customer2.getall;

import com.example.demo.customer.dtos.CustomerDto;
import com.example.demo.customer.entities.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GetAllCustomersMapper {

    CustomerDto map(Customer dto);

}
