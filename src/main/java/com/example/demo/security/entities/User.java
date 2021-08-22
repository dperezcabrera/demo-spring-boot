package com.example.demo.security.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Id
    @Column(length = 20, nullable = false, unique = true)
    private String username;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 100)
    private String email;

    @Column(length = 100)
    private String password;
}
