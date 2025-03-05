package ru.skypro.homework.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.RoleDTO;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class CustomUserDetailsManagerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomUserDetailsManager manager;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User()
                .setUsername("user")
                .setPassword("user")
                .setFirstName("first name")
                .setLastName("last name")
                .setPhone("123456789")
                .setImage("image.jpg")
                .setPassword("rawPassword")
                .setRole(RoleDTO.USER)
                .setEnabled(true);
    }

    @Test
    void createUserTest() {
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");

        manager.createUser(new CustomUserDetails(user));

        // Перехватываем объект, который передали в userRepository
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());

        User savedUser = userArgumentCaptor.getValue();
        assertEquals("first name", savedUser.getFirstName());
        assertEquals("last name", savedUser.getLastName());
        assertEquals("123456789", savedUser.getPhone());
        assertEquals("image.jpg", savedUser.getImage());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals(RoleDTO.USER, savedUser.getRole());
        assertTrue(savedUser.getEnabled());
    }

    @Test
    void shouldThrowExceptionWhenUserDetailsIsNotCustomUser() {
        UserDetails userDetails = mock(UserDetails.class);

        assertThrows(IllegalArgumentException.class, () -> manager.createUser(userDetails));

        verifyNoInteractions(userRepository);
    }

    @Test
    void updateUserTest() {
        User existingUser = new User();
        existingUser.setUsername("testuser");
        existingUser.setFirstName("old name");
        existingUser.setLastName("old last name");
        existingUser.setPhone("12345");
        existingUser.setImage("old.jpg");
        existingUser.setRole(RoleDTO.USER);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(existingUser));

        manager.updateUser(new CustomUserDetails(user));

        assertEquals("first name", existingUser.getFirstName());
        assertEquals("last name", existingUser.getLastName());
        assertEquals("123456789", existingUser.getPhone());
        assertEquals("image.jpg", existingUser.getImage());
        assertEquals(RoleDTO.USER, existingUser.getRole());

        verify(userRepository).save(existingUser);
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        manager.deleteUser("testuser");

        verify(userRepository).deleteByUsername("testuser");
    }


    @Test
    void shouldChangePasswordSuccessfully() {
        User user = new User();
        user.setPassword("encodedOldPassword");

        when(userService.getCurrentUserEntity()).thenReturn(user);
        when(passwordEncoder.matches("oldPassword", "encodedOldPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        manager.changePassword("oldPassword", "newPassword");
        assertEquals("encodedNewPassword", user.getPassword());

        verify(userRepository).save(user);
    }

    @Test
    void userExistsTest() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        boolean result =  manager.userExists("testuser");

        assertTrue(result);

        verify(userRepository, times(1)).existsByUsername("testuser");
    }

    @Test
    void shouldReturnUserDetailsWhenUserExists() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserDetails userDetails = manager.loadUserByUsername("testuser");
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());

        verify(userRepository, times(1)).findByUsername("testuser");
    }


}
