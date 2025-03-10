package ru.skypro.homework.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import ru.skypro.homework.filter.BasicAuthCorsFilter;

import static org.springframework.security.config.Customizer.withDefaults;


/**
 * Класс конфигурации безопасности веб-приложения с использованием Spring Security.
 * Этот класс настраивает аутентификацию и авторизацию, а также определяет правила для CORS и CSRF защиты.
 * Он включает настройку пароля через BCrypt и конфигурацию фильтров безопасности.
 * Этот класс обеспечивает базовую безопасность для приложения, включая настройки для аутентификации, авторизации и защиты от атак.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final BasicAuthCorsFilter basicAuthCorsFilter;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login",
            "/register"
    };

    /**
     * Настройка фильтра безопасности, включая CORS, авторизацию и аутентификацию.
     *
     * @param http объект {@link HttpSecurity} для конфигурации безопасности веб-приложения.
     * @return {@link SecurityFilterChain} с настройками безопасности.
     * @throws Exception если возникает ошибка при настройке безопасности.
     */
    @Bean

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors(cors -> cors.configurationSource(basicAuthCorsFilter.corsConfigurationSource()))
                .authorizeHttpRequests(
                        authorization ->
                                authorization
                                        .mvcMatchers(AUTH_WHITELIST)
                                        .permitAll()
                                        .mvcMatchers(HttpMethod.GET, "/ads/**").permitAll()
                                        .mvcMatchers(HttpMethod.GET, "/ads/*/comments").permitAll()
                                        .mvcMatchers(HttpMethod.PATCH, "/ads/**", "/users/**").authenticated()
                                        .mvcMatchers(HttpMethod.PUT, "/ads/**").authenticated()
                                        .mvcMatchers(HttpMethod.DELETE, "/ads/**", "/users/**").authenticated()
                                        .mvcMatchers("/ads/**", "/users/**").authenticated())
                .httpBasic(withDefaults())
                .securityContext(securityContext ->
                        securityContext.securityContextRepository(new HttpSessionSecurityContextRepository()));
        return http.build();

    }

    /**
     * Настройка кодировщика паролей для использования BCrypt.
     *
     * @return {@link PasswordEncoder}, использующий алгоритм BCrypt для хэширования паролей.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
