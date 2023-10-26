package com.example.demo.customer2.dtos;

import jakarta.validation.constraints.NotEmpty;

public record SearchCustomerDto(
        @NotEmpty String name) {

}
