package ru.skypro.homework.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.impl.CustomUserDetailsService;

import javax.servlet.http.HttpServletResponse;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserRepository repository;
    private static final String[] AUTH_WHITELIST = {
            "/login",
            "/register",
            "/ads"
    };


    @Bean
    public UserDetailsService userDetailsService (){
        return new CustomUserDetailsService(repository);
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authProvider);
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .authorizeHttpRequests(
                        authorization ->
                                authorization
                                        .antMatchers(AUTH_WHITELIST).permitAll()
                                        .antMatchers(HttpMethod.GET, "/users/**").permitAll()
                                        .antMatchers(HttpMethod.GET, "/ads/**").permitAll()
                                        .antMatchers("/ads/**", "/users/**").authenticated()
                                        .antMatchers(HttpMethod.GET, "/comments/**").authenticated()
                                        .antMatchers(HttpMethod.POST, "/ads/**", "/comments/**").hasRole("USER")
                                        .antMatchers(HttpMethod.PATCH, "/ads/**", "/comments/**").hasAnyRole("USER", "ADMIN")
                                        .antMatchers(HttpMethod.DELETE, "/ads/**", "/comments/**").hasAnyRole("USER", "ADMIN")
                                        .anyRequest().permitAll())
                .httpBasic(withDefaults())
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) ->
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Неверный логин или пароль."));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
        corsConfiguration.setAllowedMethods(List.of("GET","POST","PATCH","DELETE"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
