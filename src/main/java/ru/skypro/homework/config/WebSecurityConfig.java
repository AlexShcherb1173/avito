package ru.skypro.homework.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.skypro.homework.dto.user.Role;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/v3/api-docs",
            "/webjars/**",
            "/login",
            "/register",
            "/api-docs/**",
            "/ads",
            "/ads/image/*",
            "/users/image/*",
            "/logout"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf()
                .disable()
                .authorizeHttpRequests(authorization ->
                        authorization
                        .mvcMatchers(AUTH_WHITELIST).permitAll()                // Белый список
                        .mvcMatchers("/users/*/image").permitAll()  // Разрешаем доступ к изображениям пользователей без аутентификации
                        .anyRequest().authenticated()                           // Все остальное - под авторизацией
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic()                                                     // 3. Тип аутентификации
                .and()                              //(Spring Security ищет заголовок Authorization: Basic base64encoded)
                .userDetailsService(userDetailsService);        //Если есть → передает логин/пароль в UserDetailsService

        return httpSecurity.build();
    }

//  разрешает фронтенду на localhost:3000 обращаться к нашему API
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // разрешает фронтенд
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")); // разрешает методы
        configuration.setAllowedHeaders(Arrays.asList("*"));    // разрешает все заголовки
        configuration.setAllowCredentials(true);    // разрешает куки/авторизацию

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // применяем ко всем путям
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // безопасное хеширование паролей
    }

}
