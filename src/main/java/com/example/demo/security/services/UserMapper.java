package com.example.demo.security.services;

import com.example.demo.security.dtos.UserDto;
import com.example.demo.security.entities.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.userdetails.UserDetails;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    UserDto map(User user, List<String> roles, List<String> privileges);

    User map(UserDto user);

    default UserDetails map(User u, List<String> roles) {
        return org.springframework.security.core.userdetails.User
                .withUsername(u.getUsername())
                .password(u.getPassword())
                .roles(roles.stream().toArray(String[]::new))
                .build();
    }
}
