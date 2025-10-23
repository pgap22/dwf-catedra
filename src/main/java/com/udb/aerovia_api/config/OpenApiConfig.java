package com.udb.aerovia_api.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Aerovia API - Gestion de Boletos Aereos",
                version = "1.0.0",
                description = "API REST del sistema Aerovia para administrar aerolineas, vuelos, reservas, pagos, tripulaciones y reclamos.",
                contact = @Contact(
                        name = "Equipo Aerovia",
                        email = "soporte@aerovia.example",
                        url = "https://www.udb.edu.sv"
                ),
                license = @License(name = "Propietaria", url = "https://www.udb.edu.sv/licencias/dwf")
        ),
        externalDocs = @ExternalDocumentation(
                description = "Documentacion funcional y diagramas del proyecto",
                url = "https://drive.google.com/file/d/1hrYJrleNusqMvMgfG37ufbN0AEH5yvWA/view"
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("autenticacion")
                .pathsToMatch("/api/v1/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi catalogosApi() {
        return GroupedOpenApi.builder()
                .group("catalogos")
                .pathsToMatch(
                        "/api/v1/aerolineas/**",
                        "/api/v1/aeropuertos/**",
                        "/api/v1/aviones/**",
                        "/api/v1/asientos-avion/**",
                        "/api/v1/clases/**",
                        "/api/v1/rutas/**",
                        "/api/v1/tripulantes/**",
                        "/api/v1/tarifas/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi operacionesVueloApi() {
        return GroupedOpenApi.builder()
                .group("operaciones-vuelo")
                .pathsToMatch(
                        "/api/v1/operaciones-vuelo/**",
                        "/api/v1/operaciones-tripulacion/**",
                        "/api/v1/tarifas-operacion/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi reservasApi() {
        return GroupedOpenApi.builder()
                .group("reservas")
                .pathsToMatch(
                        "/api/v1/reservas/**",
                        "/api/v1/pagos/**",
                        "/api/v1/cancelaciones/**"
                )
                .build();
    }

    @Bean
    public GroupedOpenApi reclamosApi() {
        return GroupedOpenApi.builder()
                .group("reclamos")
                .pathsToMatch("/api/v1/reclamos/**")
                .build();
    }

    @Bean
    public GroupedOpenApi estadisticasApi() {
        return GroupedOpenApi.builder()
                .group("estadisticas")
                .pathsToMatch("/api/v1/estadisticas/**")
                .build();
    }
}
