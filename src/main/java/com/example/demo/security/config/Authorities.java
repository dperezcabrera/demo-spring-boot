package com.example.demo.security.config;

import org.springframework.stereotype.Component;

@Component
public class Authorities {

    public String[] all() {
        return new String[]{"consulta", "administrador"};
    }
}
