package com.gymnasio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI api() {
    return new OpenAPI()
      .info(new Info().title("Smart Gym API").version("v1")
        .description("Spring Boot + JPA + Security (JWT) en Swagger UI - Proyecto De Herramientas de Desarrollo"))
      .components(new Components()
        .addSecuritySchemes("bearerAuth",
          new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")))
      .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
  }
}
