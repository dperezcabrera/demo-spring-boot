package com.example.demo.security.repositories;

import com.example.demo.security.entities.UserPrivilege;
import com.example.demo.security.entities.UserPrivilegeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPrivilegeRepository extends JpaRepository<UserPrivilege, UserPrivilegeId> {

}
