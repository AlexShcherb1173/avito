package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        String email = "test@example.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        userEntity.setPassword("encodedPassword");
        userEntity.setRole(Role.USER);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserNotFound() {
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(email);
        });

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void loadUserByUsername_ShouldReturnCorrectRole_WhenUserIsAdmin() {
        String email = "admin@example.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        userEntity.setPassword("encodedPassword");
        userEntity.setRole(Role.ADMIN);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }
}