package com.example.demo.customer2.dtos;

import jakarta.validation.constraints.NotEmpty;

public record UpdateCustomerDto(
        @NotEmpty String name) {

}
