package ru.skypro.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.entity.Role;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class MyUserDetailsManager implements UserDetailsManager {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public MyUserDetailsManager(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private final Logger logger = LoggerFactory.getLogger(MyUserDetailsManager.class);

    @Override
    public void createUser(UserDetails userDetails) {
        logger.info("Method for creating new user was invoked");
        if (userExists(userDetails.getUsername())) {
            throw new IllegalArgumentException("User already exists: " + userDetails.getUsername());
        }

        User user = new User();
        user.setEmail(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());

        Role role = extractRoleFromAuthorities(userDetails.getAuthorities());
        user.setRole(role);

        userRepository.save(user);
        logger.info("New user was saved to the repository");
    }

    @Override
    public void updateUser(UserDetails userDetails) {
        logger.info("Method for updating a user was invoked");
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found: " + userDetails.getUsername()));

        user.setPassword(userDetails.getPassword());

        Role role = extractRoleFromAuthorities(userDetails.getAuthorities());
        user.setRole(role);

        userRepository.save(user);
        logger.info("User was updated");
    }

    @Override
    public void deleteUser(String username) {
        logger.info("Method for deleting a user was invoked");
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        userRepository.delete(user);
        logger.info("User was deleted");
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        logger.info("Method for changing the password was invoked");
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        String username = currentUser.getName();

        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        logger.info("The password was changed");
    }

    @Override
    public boolean userExists(String username) {
        logger.info("Method for checking whether user exists was invoked");
        return userRepository.findByEmail(username).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Method for loading a user by username was invoked");
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        logger.info("User was loaded by username");
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    private Role extractRoleFromAuthorities(Collection<? extends GrantedAuthority> authorities) {
        logger.info("Sub-method for extracting role from authorities was invoked");
        return authorities.stream().map(GrantedAuthority::getAuthority).filter(authority -> authority.startsWith("ROLE_")).map(authority -> authority.replace("ROLE_", "")).map(Role::valueOf).findFirst().orElse(Role.USER);
    }

    public User getCurrentUser() {
        logger.info("Method for getting current user was invoked");
        checkUserAuthenticated();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    public void checkUserAuthenticated() {
        logger.info("Method for checking authentication was invoked");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        logger.info("User was authenticated");
    }
}
