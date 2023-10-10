package com.example.authservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    private static final String SERVER_URL = "http://localhost:8082";
    private static final String TITLE = "Auth Service";
    private static final String VERSION = "1.0";
    private static final String DESCRIPTION = "Service for registration users and get tokens.";

    @Bean
    public OpenAPI myOpenAPI() {
        Server server = new Server();
        server.setUrl(SERVER_URL);
        Info info = new Info()
                .title(TITLE)
                .version(VERSION)
                .description(DESCRIPTION);
        return new OpenAPI().info(info).addServersItem(server);
    }
}