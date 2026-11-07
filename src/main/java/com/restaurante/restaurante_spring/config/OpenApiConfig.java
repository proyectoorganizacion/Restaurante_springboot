package com.restaurante.restaurante_spring.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de la documentación OpenAPI / Swagger UI.
 * Documentación disponible en: http://localhost:8080/swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    private static final String ESQUEMA_JWT = "bearerAuth";

    @Bean
    public OpenAPI restauranteOpenAPI() {
        return new OpenAPI()
                // 1. Información general de la API (lo que se ve arriba en Swagger UI)
                .info(new Info()
                        .title("API Restaurante")
                        .version("1.0")
                        .description("API REST para la gestión de restaurantes, platos y pedidos. "
                                + "Roles: ADMINISTRADOR, PROPIETARIO, EMPLEADO y CLIENTE. "
                                + "Para probar endpoints protegidos: haz login en POST /api/v1/auth/login, "
                                + "copia el token y pégalo en el botón Authorize."))
                // 2. Declarar que la API usa tokens JWT (agrega el botón "Authorize")
                .components(new Components()
                        .addSecuritySchemes(ESQUEMA_JWT, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Pega solo el token, sin la palabra Bearer")))
                // 3. Aplicar ese esquema a todos los endpoints
                .addSecurityItem(new SecurityRequirement().addList(ESQUEMA_JWT));
    }
}