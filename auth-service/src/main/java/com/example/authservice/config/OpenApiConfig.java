package com.example.authservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    private static final String URL = "http://localhost:8082";

    @Bean
    public OpenAPI myOpenAPI() {
        Server server = new Server();
        server.setUrl(URL);
        Info info = new Info()
                .title("Auth Service")
                .version("1.0")
                .description("Service for registration users and get tokens.");
        return new OpenAPI().info(info).addServersItem(server);
    }
}