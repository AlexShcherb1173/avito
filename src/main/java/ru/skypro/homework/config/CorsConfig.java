package ru.skypro.homework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/image/**")
                // Применяется ко всем эндпоинтам под "/image/**"
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET")
                .allowedHeaders("*")
                // allowedHeaders: Указывает разрешённые заголовки запроса - в данном случае любые.
                .allowCredentials(true);
                // Параметр allowCredentials(true) на бэкенде разрешает браузеру включать учетные данные (куки, заголовки
                // авторизации) в запрос.

        // В этом примере глобальная конфигурация CORS разрешает фронтенд-приложению, работающему на URL
        // "http://localhost:3000", отправлять GET запросы к любым эндпоинтам, начинающимся с "/image/**" на бэкенде.
    }
}
