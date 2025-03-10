package ru.skypro.homework.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Класс для настройки CORS (Cross-Origin Resource Sharing) в приложении.
 * Этот компонент конфигурирует правила доступа для определенных источников и методов,
 * а также задает заголовки, которые разрешены для использования при запросах между разными доменами.
 * <p>
 * Этот фильтр используется для настройки CORS в Spring приложении с целью обеспечения правильного взаимодействия
 * с клиентом на разных доменах.
 */
@Component
public class BasicAuthCorsFilter {

    /**
     * Конфигурирует правила CORS для приложения, включая разрешенные источники,
     * методы, заголовки и другие параметры.
     *
     * @return {@link CorsConfigurationSource} с настроенными правилами для CORS.
     */
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        // Устанавливаем разрешенные источники
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        // Устанавливаем разрешенные HTTP методы
        configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS"));
        // Разрешаем любые заголовки
        configuration.setAllowedHeaders(List.of("*"));
        // Разрешаем использование учетных данных
        configuration.setAllowCredentials(true);
        // Устанавливаем максимальное время жизни для кэширования CORS-заголовков
        configuration.setMaxAge(3600L);

        // Настроим и вернем конфигурацию для всех путей
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
