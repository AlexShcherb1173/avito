package ru.skypro.homework.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.UserServiceImpl;

@Component
public class CustomUserDetailsManager implements UserDetailsManager {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceImpl userService;

    public CustomUserDetailsManager(UserRepository userRepository, PasswordEncoder passwordEncoder, UserServiceImpl userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    public void createUser(UserDetails userDetails) {
        if (!(userDetails instanceof CustomUserDetails customUser)) {
            throw new IllegalArgumentException("UserDetails must be an instance of CustomUserDetails");
        }

        User user = new User()
                .setUsername(customUser.getUsername())
                .setFirstName(customUser.getFirstName())
                .setLastName(customUser.getLastName())
                .setPhone(customUser.getPhone())
                .setImage(customUser.getImage())
                .setPassword(passwordEncoder.encode(customUser.getPassword()))
                .setRole(customUser.getRole())
                .setEnabled(customUser.isEnabled());

        userRepository.save(user);
    }

    @Override
    public void updateUser(UserDetails userDetails) {
        if (!(userDetails instanceof CustomUserDetails customUser)) {
            throw new IllegalArgumentException("UserDetails must be an instance of CustomUserDetails");
        }

        User user = userRepository.findByUsername(customUser.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setFirstName(customUser.getFirstName());
        user.setLastName(customUser.getLastName());
        user.setPhone(customUser.getPhone());
        user.setImage(customUser.getImage());
        user.setRole(customUser.getRole());

        userRepository.save(user);
    }

    @Override
    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        User user = userService.getCurrentUserEntity();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElse(null);
        return new CustomUserDetails(user);
    }
}
