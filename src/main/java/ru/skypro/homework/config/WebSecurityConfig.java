package ru.skypro.homework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.skypro.homework.dto.Role;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/actuator/**",
            "/actuator/health",
            "/swagger-resources/**",

            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/webjars/**",

            "/",
            "/index.html",
            "/demo.html",
            "/static/**",
            "/css/**",
            "/js/**",
            "/images/**",

            "/login",
            "/register",
            "/api/register",
            "/api/login",

            "/ping",
            "/simple-test",
            "/public/**",

            "/**"
    };

//    @Bean
//    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
//        UserDetails user =
//                User.builder()
//                        .username("user@gmail.com")
//                        .password("password")
//                        .passwordEncoder(passwordEncoder::encode)
//                        .roles(Role.USER.name())
//                        .build();
//        return new InMemoryUserDetailsManager(user);
//    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf()
//                .disable()
//                .authorizeHttpRequests(
//                        authorization ->
//                                authorization
//                                        .mvcMatchers(AUTH_WHITELIST)
//                                        .permitAll()
//
//                                        .mvcMatchers(HttpMethod.OPTIONS, "/**")
//                                        .permitAll()
//
//                                        .anyRequest()
//                                        .authenticated())
//
//                .cors()
//                .and()
//                .httpBasic(withDefaults());
//        return http.build();
//    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(). disable()
//                .authorizeHttpRequests()
//                .anyRequest().permitAll()  // ⚠️ ВРЕМЕННО разрешаем всё!
//                .and()
//                .cors()
//                .and()
//                .httpBasic(withDefaults());
//        return http.build();
//    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(authorization -> authorization
                        .mvcMatchers(AUTH_WHITELIST).permitAll()
                        .mvcMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
