package ru.skypro.homework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.skypro.homework.dto.Role;
import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class WebSecurityConfig implements WebMvcConfigurer {

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login",
            "/register"
    };

    @Bean
    public JdbcUserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        if (!jdbcUserDetailsManager.userExists("user@gmail.com")) {
            UserDetails user = User.builder()
                    .username("user@gmail.com")
                    .password(passwordEncoder().encode("password"))
                    .roles(Role.USER.name())
                    .build();
            jdbcUserDetailsManager.createUser(user);
        }
        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests(
                        authorization ->
                                authorization
                                        .mvcMatchers(AUTH_WHITELIST).permitAll() //Доступ для всех
                                        .mvcMatchers("/ads/**").hasAnyRole("USER", "ADMIN") //Доступ для пользователей с ролями USER и ADMIN
                                        .mvcMatchers("/users/**").hasRole("ADMIN") //Доступ только для администраторов
                                        .anyRequest().authenticated()) // Все остальные запросы требуют аутентификации
                .cors()
                .and()
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Разрешить все запросы
                .allowedOrigins("http://localhost:3000") // Разрешить запросы с фронтенда
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
