package com.example.demo.security.domain;

import com.example.demo.security.dtos.UserDto;
import com.example.demo.security.repositories.UserRepository;
import com.example.demo.security.services.UserMapper;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserRegiterDomainService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public void register(@NonNull UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        var user = userMapper.map(userDto);
        userRepository.save(user);
    }
}
