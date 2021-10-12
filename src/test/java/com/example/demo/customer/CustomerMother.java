package com.example.demo.customer;

import com.example.demo.customer.dtos.CustomerDto;
import com.example.demo.customer.entities.Customer;

public class CustomerMother {

    public CustomerDto defaultNameCustomerDto() {
        return customerDto(1L, "no-name");
    }

    public CustomerDto johnDto() {
        return customerDto(1L, "John");
    }

    public CustomerDto john2Dto() {
        return customerDto(2L, "John");
    }

    public CustomerDto john3Dto() {
        return customerDto(3L, "John");
    }

    public CustomerDto peterDto() {
        return customerDto(4L, "Peter");
    }

    public CustomerDto notExistCustomer() {
        return customerDto(0L, "No exist");
    }

    public Customer john() {
        return new Customer(1L, "John");
    }

    public Customer john2() {
        return new Customer(2L, "John");
    }

    public Customer john3() {
        return new Customer(3L, "John");
    }

    public Customer peter() {
        return new Customer(4L, "Peter");
    }

    public Customer defaultNameCustomer() {
        return new Customer(1L, "no-name");
    }

    public CustomerDto customerWithNoId() {
        return customerDto(null, "John");
    }

    public CustomerDto customerWithNoName() {
        return customerDto(0L, null);
    }

    private CustomerDto customerDto(Long id, String name) {
        return new CustomerDto(id, name);
    }
}
