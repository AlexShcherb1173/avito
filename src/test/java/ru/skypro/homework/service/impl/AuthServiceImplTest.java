package ru.skypro.homework.service.impl;

import org.checkerframework.checker.nullness.Opt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.config.WebSecurityConfig;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserServiceConfig;
import ru.skypro.homework.service.map.UserMap;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Import(WebSecurityConfig.class)
public class AuthServiceImplTest {
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserServiceConfig userServiceConfig;
    @InjectMocks
    private AuthServiceImpl authService;
    @Spy
    private UserMap userMap;
    private User user;
    private Register register;

    @BeforeEach
    public void beforeEach() {
        user = new User();
        user.setId(1);
        user.setUsername("username");
        user.setPassword("encPassword");
        user.setFirstName("Vitaliy");
        user.setLastName("Vitalikov");
        user.setPhone("+7923-923-92-39");
        user.setRole(Role.USER);
        user.setImage(new byte[]{1, 2, 3});

        register = new Register();
        register.setUsername("username");
        register.setPassword("password");
        register.setFirstName("Vitaliy");
        register.setLastName("Vitalikov");
        register.setPhone("+7923-923-92-39");
        register.setRole(Role.USER);
    }

    @Test
    void loginTest() {
        String userName = "username";
        String password = "password";

        org.springframework.security.core.userdetails.User newUser
                = new org.springframework.security.core.userdetails.User(
                        "username",
                "password",
                new ArrayList<>()
        );
        when(userServiceConfig.findByUsername(userName)).thenReturn(Optional.of(user));
        when(userServiceConfig.loadUserByUsername(userName)).thenReturn(newUser);
        when(passwordEncoder.matches(password, newUser.getPassword())).thenReturn(true);

        assertThat(authService.login(userName, password)).isTrue();

        verify(userServiceConfig, times(1)).findByUsername(userName);
        verify(userServiceConfig, times(1)).loadUserByUsername(userName);

    }

    @Test
    void loginFailedTest() {
        String userName = "username";
        String password = "password";

        when(userServiceConfig.findByUsername(userName)).thenReturn(Optional.empty());

        assertThat(authService.login(userName, password)).isFalse();

        verify(userServiceConfig, times(1)).findByUsername(userName);
        verify(userServiceConfig, never()).loadUserByUsername(userName);

    }

    @Test
    void registerTest() {
        when(userServiceConfig.findByUsername(register.getUsername())).thenReturn(Optional.empty());

        assertThat(authService.register(register)).isTrue();

        verify(userServiceConfig, times(1)).findByUsername(register.getUsername());
        verify(userRepository, times(1)).save(any());

    }

    @Test
    void testRegisterFailed() {
        when(userServiceConfig.findByUsername(register.getUsername())).thenReturn(Optional.of(user));

        assertThat(authService.register(register)).isFalse();

        verify(userServiceConfig, times(1)).findByUsername(register.getUsername());
        verify(userRepository, never()).save(any());
    }
}
