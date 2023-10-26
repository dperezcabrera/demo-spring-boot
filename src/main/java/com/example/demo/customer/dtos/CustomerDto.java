package com.example.demo.customer.dtos;

import jakarta.validation.constraints.NotEmpty;

public record CustomerDto(
        Long id,
        @NotEmpty String name) {
    
}
