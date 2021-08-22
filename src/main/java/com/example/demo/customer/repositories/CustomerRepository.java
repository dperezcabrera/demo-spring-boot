package com.example.demo.customer.repositories;

import com.example.demo.customer.entities.Customer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select c from Customer c where c.name = :name")
    List<Customer> findByName(@Param("name") String name);
}
