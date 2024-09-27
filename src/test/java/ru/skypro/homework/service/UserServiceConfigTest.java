package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.skypro.homework.exceptions.UserNotFoundException;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceConfigTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceConfig userServiceConfig;
    private User user;

    @BeforeEach
    public void beforeEach() {
        user = new User();
        user.setId(1);
        user.setUsername("username");
        user.setPassword("encPassword");
        user.setFirstName("Vitaliy");
        user.setLastName("Vitalikov");
        user.setPhone("+7923-923-92-39");
        user.setImage(new byte[]{1,2,3});
    }

    @Test
    public void findByUserNameTest() {
        String userName = user.getUsername();
        when(userRepository.findByUsername(userName))
                .thenReturn(Optional.of(user));
        assertThat(userServiceConfig.findByUsername(userName))
                .isEqualTo(Optional.of(user));
        verify(userRepository, times(1)).findByUsername(userName);

    }

    @Test
    public void loadUserByUserNameTest() {
        String userName = user.getUsername();
        org.springframework.security.core.userdetails.User newUser =
                new org.springframework.security.core.userdetails.User(
                        "username",
                        "password",
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                );
        when(userRepository.findByUsername(userName)).thenReturn(Optional.of(user));

        assertThat(userServiceConfig.loadUserByUsername(userName)).isEqualTo(newUser);
        verify(userRepository, times(1)).findByUsername(userName);

    }

    @Test
    public void loadUserByUserNameNotFoundTest() {
        String userName = user.getUsername();

        when(userRepository.findByUsername(userName)).thenReturn(Optional.empty());

        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(()-> userServiceConfig.loadUserByUsername(userName));
        verify(userRepository, times(1)).findByUsername(userName);
    }
}
