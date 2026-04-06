package ru.avito.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.avito.entity.Role;
import ru.avito.entity.User;
import ru.avito.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .email("user@example.com")
                .password("encoded-password")
                .firstName("Ivan")
                .lastName("Ivanov")
                .phone("+79990000001")
                .role(Role.USER)
                .image("/images/users/1/avatar.jpg")
                .build();
    }

    @Test
    void loadUserByUsernameShouldReturnCustomUserDetailsWhenUserExists() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        var result = customUserDetailsService.loadUserByUsername("user@example.com");

        assertNotNull(result);
        assertInstanceOf(CustomUserDetails.class, result);

        CustomUserDetails customUserDetails = (CustomUserDetails) result;
        assertEquals(1, customUserDetails.getId());
        assertEquals("user@example.com", customUserDetails.getUsername());
        assertEquals("encoded-password", customUserDetails.getPassword());
        assertEquals(user, customUserDetails.getUser());

        assertEquals(1, customUserDetails.getAuthorities().size());
        assertTrue(
                customUserDetails.getAuthorities()
                        .stream()
                        .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"))
        );

        assertTrue(customUserDetails.isAccountNonExpired());
        assertTrue(customUserDetails.isAccountNonLocked());
        assertTrue(customUserDetails.isCredentialsNonExpired());
        assertTrue(customUserDetails.isEnabled());

        verify(userRepository).findByEmail("user@example.com");
    }

    @Test
    void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("missing@example.com")
        );

        assertEquals("User not found with email: missing@example.com", exception.getMessage());
        verify(userRepository).findByEmail("missing@example.com");
    }

    @Test
    void loadUserByUsernameShouldReturnAdminAuthorityForAdminRole() {
        User admin = User.builder()
                .id(2)
                .email("admin@example.com")
                .password("encoded-admin-password")
                .firstName("Admin")
                .lastName("User")
                .phone("+79990000002")
                .role(Role.ADMIN)
                .image(null)
                .build();

        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(admin));

        var result = customUserDetailsService.loadUserByUsername("admin@example.com");

        assertInstanceOf(CustomUserDetails.class, result);
        assertTrue(
                result.getAuthorities()
                        .stream()
                        .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))
        );

        verify(userRepository).findByEmail("admin@example.com");
    }
}