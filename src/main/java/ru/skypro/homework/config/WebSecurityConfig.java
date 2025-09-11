package ru.skypro.homework.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final DataSource dataSource;

    // Бин для загрузки пользователей из БД
    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT username, password, enabled FROM users WHERE username = ?");

        // ВАЖНО: Подставьте правильный запрос based на вашу структуру БД!
        // Этот пример для случая, когда роли хранятся в столбце role таблицы users
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT username, 'ROLE_' || role as authority FROM users WHERE username = ?");

        return jdbcUserDetailsManager;
    }

    // Бин для кодирования паролей
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // Бин AuthenticationProvider - ЯВНО указываем использование jdbcUserDetailsManager
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(jdbcUserDetailsManager(dataSource)); // Явно передаем наш jdbcUserDetailsManager
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Бин AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // Главная конфигурация безопасности
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/home", "/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Страница логина (если есть)
                        .loginProcessingUrl("/login") // URL для обработки формы
                        .usernameParameter("username") // Параметр username
                        .passwordParameter("password") // Параметр password
                        .defaultSuccessUrl("/", true) // Перенаправление после успеха
                        .failureUrl("/login?error=true") // Перенаправление при ошибке
                        .permitAll()
                )
                // Добавляем обработку JSON аутентификации
                .addFilterBefore(jsonAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .authenticationProvider(authProvider());

        return http.build();
    }

    // Фильтр для обработки JSON аутентификации
    private JsonAuthenticationFilter jsonAuthenticationFilter() throws Exception {
        JsonAuthenticationFilter filter = new JsonAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager(null));
        filter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write("{\"message\": \"Login successful\"}");
        });
        filter.setAuthenticationFailureHandler((request, response, exception) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("{\"error\": \"Invalid credentials\"}");
        });
        return filter;
    }
}