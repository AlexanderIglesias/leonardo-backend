package com.alphanet.products.leonardobackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SENASoft Metrics API")
                        .version("1.0.0")
                        .description("API for obtaining metrics of SENA apprentices, training centers and programs")
                        .contact(new Contact()
                                .name("AlphaNet Products")
                                .email("fabian.iglesias.m@gmail.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local development server"),
                        new Server()
                                .url("https://senasoft-core-dev-h2erabc0hhbcg7ee.eastus2-01.azurewebsites.net")
                                .description("Production server")
                ));
    }
}
