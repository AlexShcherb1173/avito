package ru.skypro.homework.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация OpenAPI/Swagger для документации API.
 * Настраивает метаданные и общую информацию о REST API.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Создает конфигурацию OpenAPI для документации.
     * Определяет заголовок, версию и описание API.
     *
     * @return конфигурация OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Documentation")
                        .version("1.0")
                        .description("Документация для Avito-like приложения"));
    }
}