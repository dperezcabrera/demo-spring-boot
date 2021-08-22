package com.example.demo.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
public class OpenApi30Config {

    private final Environment env;

    @Bean
    public OpenAPI openAPI() {
        final var securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(info())
                .addServersItem(new Server().url(getAppUrl()))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(components(securitySchemeName));
    }

    private String getAppUrl() {
        String url = env.getProperty("app.url");
        if (url == null) {
            return "/";
        }
        return url;
    }

    private Info info() {
        return new Info().title("Demo Spring Boot API")
                .description("# Demo Spring Boot description")
                .version("0.0.1-SNAPSHOT")
                .contact(new Contact().name("David").email("dperezcabrera@gmail.com"));
    }

    private Components components(String securitySchemeName) {
        return new Components().addSecuritySchemes(securitySchemeName,
                new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));
    }
}
