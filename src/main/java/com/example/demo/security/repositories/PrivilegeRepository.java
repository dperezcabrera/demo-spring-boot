package com.example.demo.security.repositories;

import com.example.demo.security.entities.Privilege;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, String> {

    @Query("SELECT p.id.privilege.name FROM RolePrivilege p WHERE p.id.role.name = :role")
    List<String> findByRole(@Param("role") String role);

    @Query("SELECT p.id.privilege.name FROM RolePrivilege p WHERE p.id.role.name IN :roles")
    List<String> findByRoles(@Param("roles") Collection<String> roles);

    @Query("SELECT p.id.privilege.name FROM UserPrivilege p WHERE p.id.user.username = :username")
    List<String> findByUsername(@Param("username") String username);
}
