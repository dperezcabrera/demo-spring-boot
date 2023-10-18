package com.example.demo.customer.controllers;

import com.example.demo.customer.dtos.CustomerDto;
import com.example.demo.customer.dtos.CustomerNameDto;
import com.example.demo.customer.services.CreateCustomerService;
import com.example.demo.customer.services.DeleteCustomerByIdService;
import com.example.demo.customer.services.GetAllCustomersService;
import com.example.demo.customer.services.GetCustomerByIdService;
import com.example.demo.customer.services.SearchCustomersByNameService;
import com.example.demo.customer.services.UpdateCustomerService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/customers")
public class CustomerController {

    private final UpdateCustomerService updateCustomerService;
    private final SearchCustomersByNameService searchCustomersByNameService;
    private final CreateCustomerService createCustomerService;
    private final GetAllCustomersService getAllCustomersService;
    private final GetCustomerByIdService getCustomerByIdService;
    private final DeleteCustomerByIdService deleteCustomerByIdService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerDto> getAllCustomers() {
        return getAllCustomersService.getAllCustomers();
    }

    @GetMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto getCustomerById(@PathVariable("customerId") long customerId) {
        return getCustomerByIdService.getCustomerById(customerId);
    }

    @GetMapping("/search")
    public List<CustomerDto> searchCustomersByName(@RequestParam("name") String name) {
        return searchCustomersByNameService.searchCustomersByName(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        createCustomerService.createCustomer(customerDto);
    }

    @PutMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateCustomer(@PathVariable("customerId") Long customerId, @Valid @RequestBody CustomerNameDto dto) {
        var customerDto = new CustomerDto(customerId, dto.name());
        updateCustomerService.updateCustomer(customerDto);
    }

    @DeleteMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCustomerById(@PathVariable("customerId") Long customerId) {
        deleteCustomerByIdService.deleteCustomerById(customerId);
    }
}
