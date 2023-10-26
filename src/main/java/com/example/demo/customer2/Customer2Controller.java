package com.example.demo.customer2;

import com.example.demo.customer2.dtos.CreateCustomerDto;
import com.example.demo.customer.dtos.CustomerDto;
import com.example.demo.customer2.dtos.UpdateCustomerDto;
import com.example.demo.customer2.create.CreateCustomerCommand;
import com.example.demo.customer2.delete.DeleteCustomerByIdCommand;
import com.example.demo.customer2.dtos.SearchCustomerDto;
import com.example.demo.customer2.getall.GetAllCustomersCommand;
import com.example.demo.customer2.getall.GetAllCustomersResponse;
import com.example.demo.customer2.getbyid.GetCustomerByIdCommand;
import com.example.demo.customer2.getbyid.GetCustomerByIdResponse;
import com.example.demo.customer2.searchbyname.SearchCustomerCommand;
import com.example.demo.customer2.searchbyname.SearchCustomerResponse;
import com.example.demo.customer2.update.UpdateCustomerCommand;
import jakarta.validation.Valid;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v2/customers")
public class Customer2Controller {

    private final CommandBus commandBus;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerDto> getAllCustomers() throws Exception {
        var future = new CompletableFuture<GetAllCustomersResponse>();
        commandBus.execute(new GetAllCustomersCommand(future));
        return future.get(10, TimeUnit.SECONDS).response();
    }

    @GetMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto getCustomerById(@PathVariable("customerId") long customerId) throws Exception{
        var future = new CompletableFuture<GetCustomerByIdResponse>();
        commandBus.execute(new GetCustomerByIdCommand(customerId, future));
        return future.get(10, TimeUnit.SECONDS).response();
    }

    @GetMapping("/search")
    public List<CustomerDto> searchCustomers(SearchCustomerDto searchDto) throws Exception {
        var future = new CompletableFuture<SearchCustomerResponse>();
        commandBus.execute(new SearchCustomerCommand(searchDto, future));
        return future.get(10, TimeUnit.SECONDS).response();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCustomer(@Valid @RequestBody CreateCustomerDto dto) {
        commandBus.execute(new CreateCustomerCommand(dto));
    }

    @PutMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateCustomer(@PathVariable("customerId") long customerId, @Valid @RequestBody UpdateCustomerDto dto) {
        commandBus.execute(new UpdateCustomerCommand(customerId, dto));
    }

    @DeleteMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCustomerById(@PathVariable("customerId") long customerId) {
        commandBus.execute(new DeleteCustomerByIdCommand(customerId));
    }
}
