package com.example.demo.customer2.update;

import com.example.demo.customer2.dtos.UpdateCustomerDto;
import com.example.demo.customer.entities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UpdateCustomerMapper {

    @Mapping(target = "id", ignore = true)
    void map(UpdateCustomerDto customerDto, @MappingTarget Customer customer);

}
