package ru.skypro.homework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig implements WebMvcConfigurer {

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login",
            "/register"
    };

//    @Bean
//    public JdbcUserDetailsManager userDetailsManager(DataSource dataSource) {
//        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
//
//        // Проверка, существует ли пользователь
//        if (!jdbcUserDetailsManager.userExists("user@gmail.com")) {
//            String username = "user@gmail.com";
//            String password = "hashed_password";
//            String firstName = "Имя";
//            String lastName = "Фамилия";
//            String role = "USER";
//            boolean enabled = true;
//
//            String sql = "INSERT INTO users (username, password, first_name, last_name, role, enabled) VALUES (?, ?, ?, ?, ?, ?)";
//            jdbcUserDetailsManager.getJdbcTemplate().update(sql, username, password, firstName, lastName, role, enabled);
//        }
//        return jdbcUserDetailsManager;
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(x -> x.disable())
                .authorizeHttpRequests(
                        authorization ->
                                authorization
                                        .mvcMatchers(AUTH_WHITELIST)
                                        .permitAll()
                                        .mvcMatchers("/ads/**", "/users/**")
                                        .authenticated())
                .cors()
                .and()
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // Разрешаем CORS для всех путей
//                .allowedOrigins("http://localhost:3000") // Разрешаем запросы с этого домена
//                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // Разрешаем методы
//                .allowCredentials(true); // Разрешаем отправку куки
//    }
}
