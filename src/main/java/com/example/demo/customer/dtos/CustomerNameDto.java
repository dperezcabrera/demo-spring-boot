package com.example.demo.customer.dtos;

import jakarta.validation.constraints.NotEmpty;

public record CustomerNameDto(
        @NotEmpty String name) {

}
