package com.example.demo.customer2.getall;

import java.util.concurrent.CompletableFuture;

public record GetAllCustomersCommand(CompletableFuture<GetAllCustomersResponse> callback) {

}
