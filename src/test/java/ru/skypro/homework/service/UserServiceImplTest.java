package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.generatedDto.UserDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.UserHasNoRightsException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.mapper.UserMapperImpl;
import ru.skypro.homework.repository.AvatarRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
//    @Mock
//    private AvatarRepository avatarRepository;
    @Mock
    private UserRepository userRepository;
    @Spy
    private UserMapper userMapper = new UserMapperImpl();
    @InjectMocks
    private UserServiceImpl out;

    private User testUser;
    private Authentication auth;

    @BeforeEach
    void init() {
        testUser = new User();
        testUser.setId(42L);
        testUser.setUsername("test@test.com");

        auth = new UsernamePasswordAuthenticationToken(testUser, null);
    }

    @Test
    void shouldThrowUserNotFoundException_whenGetUserByUsernameNotInDB() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> out.getUserDtoByUsername("Wrong email"));
    }

    @Test
    void shouldReturnUserDto_whenGetUserByUsername() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(testUser));
        UserDto result = out.getUserDtoByUsername(testUser.getUsername());

        assertThat(result).isNotNull();
        assertThat((long) result.getId()).isEqualTo(testUser.getId());
        assertThat(result.getEmail()).isEqualTo(testUser.getUsername());
        assertThat(result.getLastName()).isNull();
    }

    @Test
    void shouldReturnUser_whenGetUser() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));
        User result = out.getUser(testUser.getUsername());

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(testUser);
    }

    @Test
    void shouldThrowUserHasNoRightsException_whenUsernameDoesNotMatchAuthAndNotAdmin() {
        assertThatExceptionOfType(UserHasNoRightsException.class)
                .isThrownBy(() -> out.checkUserHasPermit(auth, testUser.getUsername()));
    }

//    @Test
//    void shouldExecuteSaveOnce_whenUpdateUser() {
//        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));
//        when(userRepository.save(testUser)).thenReturn(testUser);
//
//        UserDto result = out.updateUser(userMapper.userToUserDto(testUser));
//
//        verify(userRepository, atMostOnce()).save(testUser);
//
//        assertThat(result).isNotNull();
//    }
}