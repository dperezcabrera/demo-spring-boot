package com.example.demo.customer.services;

import com.example.demo.customer.dtos.CustomerDto;
import com.example.demo.customer.dtos.CustomerNameDto;
import com.example.demo.customer.entities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto map(Customer customer);

    @Mapping(target = "id", ignore = true)
    Customer map(String name);
    
    @Mapping(target = "id", ignore = true)
    Customer map(CustomerNameDto customerDto, @MappingTarget Customer customer);
    
    CustomerNameDto map(CustomerDto customer);
}
