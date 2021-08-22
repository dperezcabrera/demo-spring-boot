package com.example.demo.security.repositories;

import com.example.demo.security.entities.UserRole;
import com.example.demo.security.entities.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

}
