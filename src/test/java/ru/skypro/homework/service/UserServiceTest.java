package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.Role;

import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.ImageEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.skypro.homework.service.ServiceTestFabric.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, imageRepository, passwordEncoder, userMapper);
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn(TEST_USERNAME);

    }

    @Test
    public void testLoadUserByUsernameOk() {
        UserEntity user = new UserEntity();
        user.setUsername(TEST_USERNAME);
        user.setRole(Role.USER);
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(TEST_USERNAME);

        assertEquals(TEST_USERNAME, userDetails.getUsername());
        assertEquals(Role.USER.name(), userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    public void testCreateUserIsOk() {
        UserEntity user = new UserEntity();
        user.setUsername(TEST_USERNAME);
        user.setPassword(TEST_PASSWORD);

        userService.createUser(user);

        verify(userRepository).save(user);
        verify(passwordEncoder).encode(TEST_PASSWORD);
    }

    @Test
    public void testGetCurrentUsernameOk() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn(TEST_USERNAME);

        String currentUsername = userService.getCurrentUsername();

        assertEquals(TEST_USERNAME, currentUsername);
    }


    @Test
    public void testGetCurrentUserRoleOk() {
        String role = "ROLE_USER";
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        GrantedAuthority authority = new SimpleGrantedAuthority(role);
        List<GrantedAuthority> authorities = Arrays.asList(authority);
        when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);

        String currentUserRole = userService.getCurrentUserRole();

        assertEquals(role, currentUserRole);
    }

    @Test
    public void testSetPassword() {

        String newPassword = "newpassword";
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UserEntity currentUser = Mockito.mock(UserEntity.class);
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(currentUser));
        when(passwordEncoder.matches(TEST_PASSWORD, currentUser.getPassword())).thenReturn(true);
        NewPasswordDto newPasswordDto = new NewPasswordDto();
        newPasswordDto.setCurrentPassword(TEST_PASSWORD);
        newPasswordDto.setNewPassword(newPassword);
        UserServiceImpl userService = new UserServiceImpl(userRepository, null, passwordEncoder, null);

        boolean passwordChanged = userService.setPassword(newPasswordDto);

        assertTrue(passwordChanged);
        verify(passwordEncoder).encode(newPassword);
        verify(currentUser).setPassword(passwordEncoder.encode(newPassword));
        assertEquals(passwordEncoder.encode(newPassword), currentUser.getPassword());
    }

    @Test
    public void testGetUser() {

        UserEntity currentUser = Mockito.mock(UserEntity.class);
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(currentUser));
        UserServiceImpl userService = new UserServiceImpl(userRepository, null, null, userMapper);
        UserDto userDto = userService.getUser();

        assertNotNull(userDto);
        verify(userMapper).toUserDto(any(UserDto.class), eq(currentUser));
    }

    @Test
    public void testUpdateUserOk() {
        UserDto userDto = new UserDto();
        Optional<UserEntity> currentUser = Optional.of(Mockito.mock(UserEntity.class));
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(currentUser);
        UserServiceImpl userService = new UserServiceImpl(userRepository, null, null, userMapper);
        boolean updated = userService.updateUser(userDto);

        assertTrue(updated);
        verify(userMapper).toUser(any(UserEntity.class), eq(userDto));
    }

    @Test
    public void testUpdateUserNotExist() {
        UserDto userDto = new UserDto();
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());
        UserServiceImpl userService = new UserServiceImpl(userRepository, null, null, userMapper);
        boolean updated = userService.updateUser(userDto);

        assertFalse(updated);
    }

    @Test
    public void testGetUserImageOk() {
        ImageEntity image = Mockito.mock(ImageEntity.class);
        UserEntity user = Mockito.mock(UserEntity.class);
        when(userRepository.findById(TEST_ID)).thenReturn(Optional.of(user));
        when(user.getImage()).thenReturn(image);

        UserServiceImpl userService = new UserServiceImpl(userRepository, null, null, null);
        ImageEntity result = userService.getUserImage(TEST_ID);

        assertEquals(image, result);
    }

    @Test
    public void testUpdateUserImageOk() throws IOException {
        UserEntity user = Mockito.mock(UserEntity.class);
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(user));
        MultipartFile file = Mockito.mock(MultipartFile.class);
        when(file.getContentType()).thenReturn(TEST_FILE_CONTENT_TYPE);
        when(file.getBytes()).thenReturn(TEST_FILE_CONTENT);

        UserServiceImpl userService = new UserServiceImpl(userRepository, imageRepository, null, null);
        userService.updateUserImage(file);

        verify(imageRepository).save(any(ImageEntity.class));
        verify(user).setImage(any(ImageEntity.class));
    }

}
