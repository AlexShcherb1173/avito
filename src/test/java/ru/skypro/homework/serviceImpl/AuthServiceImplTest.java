package ru.skypro.homework.serviceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.config.MyUserDetailsService;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.UserAlreadyAddException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.AuthServiceImpl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static ru.skypro.homework.dto.Role.ADMIN;
import static ru.skypro.homework.dto.Role.USER;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private MyUserDetailsService userDetailsService;
    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper mapper;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    public void testLoginSuccess() {

        String username = "testuser";
        String password = "password";
        UserDetails userDetails = mock(UserDetails.class);


        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(encoder.matches(password, userDetails.getPassword())).thenReturn(true);


        assertTrue(authService.login(username, password));
    }

    @Test
    public void testLoginBadCredentials() {

        String username = "testuser";
        String password = "wrongPassword";
        UserDetails userDetails = mock(UserDetails.class);


        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(encoder.matches(password, userDetails.getPassword())).thenReturn(false);


        assertThrows(BadCredentialsException.class, () -> authService.login(username, password));
    }

    @Test
    public void testRegisterSuccess() {

        Register register = new Register("testuser", "password", "Misha", "Vatex", "misha.vatex@example.com", ADMIN);
        User user = mock(User.class);
        user.setEmail(register.getUsername());
        user.setPassword(register.getPassword());
        user.setRole(register.getRole());

        when(mapper.registerToUser(register)).thenReturn(user);
        when(repository.existsUserByEmailIgnoreCase(user.getEmail())).thenReturn(false);


        assertTrue(authService.register(register));
        verify(encoder).encode(register.getPassword());
        verify(user).setPassword(anyString());
        verify(repository).save(user);
    }

    @Test
    public void testRegisterUserAlreadyExists() {

        Register register = new Register("testuser", "password", "Misha", "Vatex", "misha.vatex@example.com", USER);
        User user = mock(User.class);


        when(mapper.registerToUser(register)).thenReturn(user);
        when(repository.existsUserByEmailIgnoreCase(user.getEmail())).thenReturn(true);


        assertThrows(UserAlreadyAddException.class, () -> authService.register(register));
    }


}
