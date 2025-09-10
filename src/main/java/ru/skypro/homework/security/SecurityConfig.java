package ru.skypro.homework.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.skypro.homework.filter.BasicAuthCorsFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {


    private final UserDetailsService userDetailsService;
    private final BasicAuthCorsFilter basicAuthCorsFilter;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**",
            "/login",
            "/register"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider(PasswordEncoder encoder) {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(userDetailsService);
        p.setPasswordEncoder(encoder);
        return p;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(a -> a
                        .mvcMatchers(AUTH_WHITELIST).permitAll()
                        .mvcMatchers(HttpMethod.GET, "/ads").permitAll()
                        .mvcMatchers("/ads/**", "/users/**", "/profile/**").authenticated()
                )
                .userDetailsService(userDetailsService)
                .cors().and()
                .httpBasic();
        http.addFilterAfter(basicAuthCorsFilter, BasicAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        jdbcUserDetailsManager.setUserExistsSql("SELECT username FROM users WHERE username = ?");
        jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT username, password, true as enabled FROM users WHERE username = ?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT u.username, ur.roles as authority FROM users u " +
                        "JOIN user_roles ur ON u.id = ur.user_id WHERE u.username = ?");


        return jdbcUserDetailsManager;
    }
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}