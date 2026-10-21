package com.dinet.solicitudes.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI solicitudesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Solicitudes")
                        .version("1.0.0")
                        .description(
                                "API REST para registrar, listar y consultar solicitudes."
                        ));
    }
}