package com.softkaU.Reto.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Configuración de CORS.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200") // Permite solicitudes CORS desde localhost:4200
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS") // Permite estos métodos
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}