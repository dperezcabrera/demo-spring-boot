package com.example.demo.security.repositories;

import com.example.demo.security.entities.Role;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    @Query("SELECT r.id.role.name FROM UserRole r WHERE r.id.user.username = :username")
    List<String> findByUsername(@Param("username") String username);
}
