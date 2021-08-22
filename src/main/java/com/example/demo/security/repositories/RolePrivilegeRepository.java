package com.example.demo.security.repositories;

import com.example.demo.security.entities.RolePrivilege;
import com.example.demo.security.entities.RolePrivilegeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, RolePrivilegeId> {

}
