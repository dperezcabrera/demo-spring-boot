package com.example.demo.security.dtos;

import jakarta.validation.constraints.NotEmpty;

public record UserDto(String username,
    @NotEmpty String name,
    @NotEmpty String email,
    @NotEmpty String password) {

}
