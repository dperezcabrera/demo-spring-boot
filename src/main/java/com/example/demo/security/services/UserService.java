package com.example.demo.security.services;

import com.example.demo.security.dtos.UserDto;
import com.example.demo.security.entities.User;
import com.example.demo.security.repositories.PrivilegeRepository;
import com.example.demo.security.repositories.RoleRepository;
import com.example.demo.security.repositories.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final PrivilegeRepository privilegeRepository;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public Optional<UserDto> findByUsername(@NonNull String username) {
        return userRepository.findById(username.toLowerCase()).map(this::map);
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return userRepository.findById(username.toLowerCase())
                .map(u -> userMapper.map(u, roleRepository.findByUsername(username.toLowerCase())))
                .orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found"));
    }

    private UserDto map(User user) {
        var username = user.getUsername().toLowerCase();
        var roles = roleRepository.findByUsername(username);
        var privileges = getPrivileges(username, roles);
        return userMapper.map(user, roles, privileges);
    }

    private List<String> getPrivileges(String username, List<String> roles) {
        var privileges = privilegeRepository.findByUsername(username);
        if (!roles.isEmpty()) {
            privileges = Stream.of(privileges, privilegeRepository.findByRoles(roles)).flatMap(List::stream).distinct().collect(Collectors.toList());
        }
        return privileges;
    }
}
