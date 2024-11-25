package ru.skypro.homework.serviceImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.GetAuthentication;
import ru.skypro.homework.config.UserDetailsImpl;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.user.UpdateUser;
import ru.skypro.homework.dto.user.UserDTO;
import ru.skypro.homework.entity.Avatar;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.IncorrectPasswordException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AvatarService;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.io.IOException;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AvatarService avatarService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private GetAuthentication getAuthentication;


    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetailsImpl userDetails;
    @Mock
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        when(authentication.getName()).thenReturn("testuser");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        user.setFirstName("testuser");
        when(userDetails.getUser()).thenReturn(user);
        when(authentication.getPrincipal()).thenReturn(userDetails);
    }

    @Test
    public void setPassword_CorrectPassword_SuccessfullyUpdated() {
        NewPassword newPassword = new NewPassword("currentPassword", "newPassword");

        user.setPassword("encodedCurrentPassword");

        when(passwordEncoder.matches("currentPassword", "encodedCurrentPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(getAuthentication.getAuthenticationUser("testuser")).thenReturn(user);

        userService.setPassword(newPassword, authentication);

        verify(passwordEncoder).matches("currentPassword", "encodedCurrentPassword");
        verify(passwordEncoder).encode("newPassword");

        verify(userRepository).save(user);

        Assertions.assertEquals("encodedNewPassword", user.getPassword());
    }

    @Test
    public void setPassword_IncorrectPassword_ThrowException() {
        NewPassword newPassword = new NewPassword("incorrectPassword", "newPassword");
        user.setPassword("encodedCurrentPassword");

        when(passwordEncoder.matches("incorrectPassword", "encodedCurrentPassword")).thenReturn(false);
        when(getAuthentication.getAuthenticationUser("testuser")).thenReturn(user);

        assertThrows(IncorrectPasswordException.class, () -> userService.setPassword(newPassword, authentication));

        verify(passwordEncoder).matches("incorrectPassword", user.getPassword());
        verify(userRepository, never()).save(user);
    }

    @Test
    public void getUser_ReturnUserDTO() {

        UserDTO userDTO = new UserDTO();

        when(userMapper.userToUserDto(user)).thenReturn(userDTO);
        when(getAuthentication.getAuthenticationUser("testuser")).thenReturn(user);

        UserDTO result = userService.getUser(authentication);

        Assertions.assertEquals(userDTO, result);
    }

    @Test
    public void updateUserInfo_SuccessfullyUpdated() {

        UpdateUser update = new UpdateUser("Misha", "Voyteh", "123456789");

        when(getAuthentication.getAuthenticationUser("testuser")).thenReturn(user);

        userService.updateUserInfo(update, authentication);

        verify(userRepository).save(user);
        Assertions.assertEquals(update.getFirstName(), user.getFirstName());
        Assertions.assertEquals(update.getLastName(), user.getLastName());
        Assertions.assertEquals(update.getPhone(), user.getPhone());
    }

    @Test
    public void updateAvatar_SuccessfullyUpdated() throws IOException {

        MultipartFile multipartFile = mock(MultipartFile.class);
        Avatar avatar = new Avatar();

        when(avatarService.uploadAvatar(multipartFile)).thenReturn(avatar);
        when(getAuthentication.getAuthenticationUser("testuser")).thenReturn(user);

        userService.updateUserAvatar(multipartFile, authentication);

        verify(avatarService).uploadAvatar(multipartFile);
        verify(userRepository).save(user);
        Assertions.assertEquals(avatar, user.getAvatar());
    }

    @Test
    public void updateUserAvatar_RemoveOldAvatar() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        Avatar avatar = new Avatar();
        Avatar oldAvatar = new Avatar();

        when(avatarService.uploadAvatar(multipartFile)).thenReturn(avatar);
        when(getAuthentication.getAuthenticationUser("testuser")).thenReturn(user);
        user.setAvatar(oldAvatar);

        userService.updateUserAvatar(multipartFile, authentication);

        verify(avatarService).uploadAvatar(multipartFile);
        verify(userRepository).save(user);
        verify(avatarService).removeAvatar(oldAvatar);
    }

}