package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private ImageServiceImpl imageService;
    @Mock
    private Authentication authentication;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void setPasswordTest() {
        User user = testUser();
        NewPasswordDto newPassword = new NewPasswordDto();
        newPassword.setCurrentPassword(passwordEncoder.encode(user.getPassword()));
        newPassword.setNewPassword("newpassword");

        given(userRepository.findUserByEmail(anyString())).willReturn(Optional.of(user));

        User updatedUser = user;
        user.setPassword(newPassword.getNewPassword());

        when(passwordEncoder.matches(
                eq(passwordEncoder.encode(user.getPassword())),
                eq(newPassword.getNewPassword()))
        ).thenReturn(true);
        given(userRepository.save(any(User.class))).willReturn(updatedUser);
        given(authentication.getName()).willReturn(user.getEmail());

        userService.setPassword(newPassword, authentication);

        verify(userRepository, times(1)).save(any(User.class));

    }
    @Test
    public void getAuthUserInfoTest() {

        when(authentication.getName()).thenReturn(testUser().getEmail());
        when(userRepository.findUserByEmail(authentication.getName())).thenReturn(Optional.ofNullable(testUser()));

        User result = userService.getAuthUserInfo(authentication);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testUser().getId());
        assertThat(result.getEmail()).isEqualTo(testUser().getEmail());
        assertThat(result.getFirstName()).isEqualTo(testUser().getFirstName());
        assertThat(result.getLastName()).isEqualTo(testUser().getLastName());
        assertThat(result.getPassword()).isEqualTo(testUser().getPassword());
        assertThat(result.getPhone()).isEqualTo(testUser().getPhone());
        assertThat(result.getRole()).isEqualTo(testUser().getRole());
        assertThat(result.getAvatar().getId()).isEqualTo(testUser().getAvatar().getId());
        assertThat(result.getAvatar().getFilePath()).isEqualTo(testUser().getAvatar().getFilePath());
        assertThat(result.getAvatar().getFileExtension()).isEqualTo(testUser().getAvatar().getFileExtension());
        assertThat(result.getAvatar().getFileSize()).isEqualTo(testUser().getAvatar().getFileSize());
        assertThat(result.getAvatar().getMediaType()).isEqualTo(testUser().getAvatar().getMediaType());

        verify(userRepository, times(1)).findUserByEmail(anyString());
    }

    @Test
    public void updateAuthUserInfoTest() {
        User user = testUser();
        UpdateUserDto updateUserDto = updateUserDto();

        given(userRepository.findUserByEmail(anyString())).willReturn(Optional.of(user));

        User updatedUser = user;
        user.setFirstName(updateUserDto.getFirstName());
        user.setLastName(updateUserDto.getLastName());
        user.setPhone(updateUserDto.getPhone());

        given(userRepository.save(any(User.class))).willReturn(updatedUser);
        given(authentication.getName()).willReturn(user.getEmail());
        given(userMapper.userToUpdateUserDto(any(User.class))).willReturn(updateUserDto);

        UpdateUserDto result = userService.updateAuthUserInfo(updateUserDto, authentication);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(updateUserDto.getFirstName());
        assertThat(result.getLastName()).isEqualTo(updateUserDto.getLastName());
        assertThat(result.getPhone()).isEqualTo(updateUserDto.getPhone());

        assertThat(user.getFirstName()).isEqualTo(updateUserDto().getFirstName());
        assertThat(user.getLastName()).isEqualTo(updateUserDto().getLastName());
        assertThat(user.getPhone()).isEqualTo(updateUserDto().getPhone());
        assertThat(user.getId()).isEqualTo(testUser().getId());

        verify(userRepository, times(1)).findUserByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
        verify(authentication, times(1)).getName();
        verify(userMapper, times(1)).userToUpdateUserDto(any(User.class));
    }

    @Test
    public void updateAvatarTest() throws IOException {

        MultipartFile imageFile = new MockMultipartFile("avatar", avatar().getFilePath(), avatar().getFileExtension(), "test".getBytes());

        when(authentication.getName()).thenReturn(testUser().getEmail());
        when(userRepository.findUserByEmail(authentication.getName())).thenReturn(Optional.ofNullable(testUser()));
        when(imageService.saveImageFile(any(MultipartFile.class))).thenReturn(avatar());

        boolean result = userService.updateAvatar(imageFile, authentication);

        verify(userRepository, times(1)).saveAndFlush(any(User.class));
        assertTrue(result);

    }


    private User testUser() {

        User user = new User();

        user.setId(1);
        user.setEmail("email");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPassword("password");
        user.setPhone("+79991112233");
        user.setRole(Role.USER);
        user.setAvatar(avatar());

        return user;
    }

    private Image avatar() {

        Image avatar = new Image();

        avatar.setId(10);
        avatar.setFilePath("image\\54-min.jpg");
        avatar.setFileExtension("jpg");
        avatar.setFileSize(100);
        avatar.setMediaType("mediaType");

        return avatar;
    }

    private Authentication auth() {

        Authentication auth = new UsernamePasswordAuthenticationToken(testUser(), null);

        return auth;
    }

    private UpdateUserDto updateUserDto() {

        UpdateUserDto updateUserDto = new UpdateUserDto();

        updateUserDto.setFirstName("new firstname");
        updateUserDto.setLastName("new lastname");
        updateUserDto.setPhone("+79990009988");

        return updateUserDto;
    }
}
