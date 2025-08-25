package com.alphanet.products.leonardobackend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Value("${api.security.enabled:true}")
    private boolean securityEnabled;

    private static final String API_KEY_SECURITY_SCHEME = "ApiKeyAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                        .title("SENASoft Metrics API")
                        .version("1.0.0")
                        .description("""
                                API for obtaining metrics of SENA apprentices, training centers and programs.
                                
                                ## Authentication
                                This API uses API Key authentication. Include your API key in the request header:
                                ```
                                X-API-Key: your_api_key_here
                                ```
                                
                                ## Public Endpoints
                                The following endpoints are publicly accessible:
                                - `/actuator/health` - Health check
                                - `/actuator/info` - Application information
                                - `/swagger-ui/**` - API documentation
                                - `/api-docs/**` - OpenAPI specification
                                
                                ## Protected Endpoints
                                All `/api/v1/**` endpoints require valid API key authentication.
                                """)
                        .contact(new Contact()
                                .name("AlphaNet Products")
                                .email("fabian.iglesias.m@gmail.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));

        // Add security configuration if enabled
        if (securityEnabled) {
            openAPI.components(new Components()
                    .addSecuritySchemes(API_KEY_SECURITY_SCHEME, new SecurityScheme()
                            .type(SecurityScheme.Type.APIKEY)
                            .in(SecurityScheme.In.HEADER)
                            .name("X-API-Key")
                            .description("API Key for authentication. Contact administrator to obtain your key.")))
                    .addSecurityItem(new SecurityRequirement().addList(API_KEY_SECURITY_SCHEME));
        }

        // Add servers based on profile
        if ("aws".equals(activeProfile)) {
            openAPI.servers(List.of(
                new Server().url("https://3.134.102.125.nip.io").description("Production Server")
            ));
        } else {
            openAPI.servers(List.of(
                new Server().url("http://localhost:8080").description("Local Development")
            ));
        }

        return openAPI;
    }
}
