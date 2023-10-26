package com.example.demo.customer2.create;

import com.example.demo.customer2.dtos.CreateCustomerDto;
import com.example.demo.customer.entities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreateCustomerMapper {

    @Mapping(target = "id", ignore = true)
    Customer map(CreateCustomerDto dto);

}
