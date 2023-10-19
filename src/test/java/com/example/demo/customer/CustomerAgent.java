package com.example.demo.customer;

import com.example.demo.customer.dtos.CustomerDto;
import com.example.demo.security.config.JwtTokenManager;
import com.example.demo.security.dtos.CredentialsDto;
import com.example.demo.security.dtos.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@Component
@RequiredArgsConstructor
public class CustomerAgent {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    
    private final String username = "user1";
    private final String password = "1";
    private String authToken = "ignored";

    public CustomerAgent registered() {
        try {
            mockMvc.perform(post("/api/v1/user/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(new UserDto(username, username, username + "@email.com", password))));
            return this;
        } catch (Exception e) {
            throw new CustomerAgentException("Error en register", e);
        }
    }

    public CustomerAgent authenticated() {
        try {
            authToken = mockMvc.perform(post("/api/v1/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(new CredentialsDto(username, password))))
                    .andReturn().getResponse().getHeader(JwtTokenManager.AUTH_HEADER_KEY);
            return this;
        } catch (Exception e) {
            throw new CustomerAgentException("Error en register", e);
        }
    }

    public ResultActions updateCustomer(@NonNull CustomerDto customer) {
        try {
            return mockMvc.perform(put("/api/v1/customers/{customerId}", customer.id())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(JwtTokenManager.AUTH_HEADER_KEY, authToken)
                    .content(objectMapper.writeValueAsString(customer)));
        } catch (Exception e) {
            throw new CustomerAgentException("Error en updateCustomer", e);
        }
    }

    public ResultActions createCustomer(@NonNull CustomerDto customer) {
        try {
            return mockMvc.perform(post("/api/v1/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(JwtTokenManager.AUTH_HEADER_KEY, authToken)
                    .content(objectMapper.writeValueAsString(customer)));
        } catch (Exception e) {
            throw new CustomerAgentException("Error en createCustomer", e);
        }
    }

    public CustomerDto getCustomer(@NonNull Long customerId) {
        try {
            var bodyResponse = mockMvc.perform(get("/api/v1/customers/{customerId}", customerId)
                    .header(JwtTokenManager.AUTH_HEADER_KEY, authToken))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            return objectMapper.readValue(bodyResponse, CustomerDto.class);
        } catch (Exception e) {
            throw new CustomerAgentException("Error en getCustomer", e);
        }
    }

    public List<CustomerDto> searchCustomersByName(@NonNull String name) throws Exception {
        String bodyResponse = mockMvc.perform(get("/api/v1/customers/search?name={named}", name)
                .header(JwtTokenManager.AUTH_HEADER_KEY, authToken))
                .andReturn().getResponse().getContentAsString();
        return Arrays.asList(objectMapper.readValue(bodyResponse, CustomerDto[].class));
    }
}
