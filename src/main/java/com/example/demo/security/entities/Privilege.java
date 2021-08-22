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
@Table(name = "privileges")
@NoArgsConstructor
@AllArgsConstructor
public class Privilege implements Serializable {

    @Id
    @Column(length = 128)
    private String name;
}
