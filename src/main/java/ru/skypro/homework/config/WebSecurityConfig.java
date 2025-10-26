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
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.skypro.homework.dto.user.Role;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/**",           // Добавьте это
            "/v3/api-docs/**",          // Добавьте это
            "/v3/api-docs",
            "/webjars/**",
            "/login",
            "/register",
            "/api-docs/**",              // Добавьте это
            "/ads",
            "/ads/image/**"
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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeHttpRequests(authorization -> authorization
                        .antMatchers(AUTH_WHITELIST).permitAll()                // 1. Белый список
                        .antMatchers("/admin/**").hasRole("ADMIN")   // 2. Проверка ролей
                        .anyRequest().authenticated()                           // 3. Все остальное - под авторизацией
                )
                .cors()
                .and()
                .httpBasic()                                                     // 4. Тип аутентификации
                .and()                              //(Spring Security ищет заголовок Authorization: Basic base64encoded)
                .userDetailsService(userDetailsService);        //Если есть → передает логин/пароль в UserDetailsService

        return httpSecurity.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
