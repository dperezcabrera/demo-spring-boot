package com.example.demo.security.entities;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "role_privileges")
@NoArgsConstructor
@AllArgsConstructor
public class RolePrivilege extends AuditableEntity implements Serializable {

    @EmbeddedId
    private RolePrivilegeId id;
}
