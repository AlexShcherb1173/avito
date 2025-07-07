package ru.skypro.homework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.Assert;
import ru.skypro.homework.model.UserModel;
import ru.skypro.homework.repository.UserRepository;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class WebSecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login",
            "/register"
    };

    @Bean
    public UserDetailsManager  userDetailsManager() {

        return new UserDetailsManager() {

            private final Logger logger = LoggerFactory.getLogger(UserDetailsManager.class);
            private AuthenticationManager authenticationManager;


            @Autowired
            private PasswordEncoder passwordEncoder;

            @Autowired
            private UserRepository userRepository;

            @Override
            public void createUser(UserDetails user) {
                UserModel userModel = (UserModel) user;
                this.userRepository.save(userModel);
            }

            @Override
            public void updateUser(UserDetails user) {
                UserModel userModel = (UserModel) user;
                this.userRepository.save(userModel);
            }

            @Override
            public void deleteUser(String username) {
                UserModel user = this.userRepository.findByEmail(username);
                if (user != null) {
                    this.userRepository.delete(user);
                }
            }

            @Override
            public void changePassword(String oldPassword, String newPassword) {
                Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
                if (currentUser == null) {
                    throw new AccessDeniedException("Can't change password as no Authentication object found in context for current user.");
                } else {
                    String username = currentUser.getName();
                    this.logger.debug("Changing password for user '" + username + "'");
                    if (this.authenticationManager != null) {
                        this.logger.debug("Reauthenticating user '" + username + "' for password change request.");
                        this.authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(username, oldPassword));
                    } else {
                        this.logger.debug("No authentication manager set. Password won't be re-checked.");
                    }

                    UserModel user = this.userRepository.findByEmail(username);
                    Assert.state(user != null, "Current user doesn't exist in database.");
                    user.setPassword(this.passwordEncoder.encode(newPassword));
                    this.userRepository.save(user);
                }
            }

            @Override
            public boolean userExists(String username) {
                return loadUserByUsername(username) != null;
            }

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return this.userRepository.findByEmail(username);
            }

            public void setAuthenticationManager(AuthenticationManager authenticationManager) {
                this.authenticationManager = authenticationManager;
            }
        };

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
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

}
