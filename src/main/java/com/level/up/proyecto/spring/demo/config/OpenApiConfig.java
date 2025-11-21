package com.level.up.proyecto.spring.demo.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API de Productos - LevelUp")
                .version("1.0")
                .description("API REST para gestión de productos de ejemplo - Spring Boot, H2, Swagger")
                .contact(new Contact()
                    .name("Tu Nombre")
                    .email("tu.email@ejemplo.com")
                )
            );
    }
}
