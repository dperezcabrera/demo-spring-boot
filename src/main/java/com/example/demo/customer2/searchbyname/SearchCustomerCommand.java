package com.example.demo.customer2.searchbyname;

import com.example.demo.customer2.dtos.SearchCustomerDto;
import java.util.concurrent.CompletableFuture;

public record SearchCustomerCommand(
        SearchCustomerDto searchParams,
        CompletableFuture<SearchCustomerResponse> callback) {

}
