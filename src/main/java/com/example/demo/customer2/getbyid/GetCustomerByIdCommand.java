package com.example.demo.customer2.getbyid;

import java.util.concurrent.CompletableFuture;

public record GetCustomerByIdCommand(
        long customerId,
        CompletableFuture<GetCustomerByIdResponse> callback) {
    

}
