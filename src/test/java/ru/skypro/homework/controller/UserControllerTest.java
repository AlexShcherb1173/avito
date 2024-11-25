package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.user.UpdateUser;
import ru.skypro.homework.dto.user.UserDTO;
import ru.skypro.homework.entity.Avatar;
import ru.skypro.homework.service.impl.AvatarServiceImpl;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserServiceImpl userService;
    @Mock
    private AvatarServiceImpl avatarService;
    @InjectMocks
    private UserController userController;

    @Test
    public void testUpdatePassword() {

        NewPassword newPassword = new NewPassword();
        Authentication authentication = mock(Authentication.class);

        ResponseEntity<?> response = userController.updatePassword(newPassword, authentication);

        verify(userService).setPassword(newPassword, authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetInformationByAuthorizedUser() {

        Authentication authentication = mock(Authentication.class);
        UserDTO userDTO = mock(UserDTO.class);

        when(userService.getUser(authentication)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.getInformationByAuthorizedUser(authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    public void testUpdateInformationFromAuthorizedUser() {

        UpdateUser updateUser = mock(UpdateUser.class);
        Authentication authentication = mock(Authentication.class);

        when(userService.updateUserInfo(updateUser, authentication)).thenReturn(updateUser);

        ResponseEntity<UpdateUser> response = userController.updateInformationFromAuthorizedUser(updateUser, authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updateUser, response.getBody());
    }

    @Test
    public void testUpdateAvatarFromAuthorizedUser() throws IOException {

        MultipartFile image = mock(MultipartFile.class);
        Authentication authentication = mock(Authentication.class);

        ResponseEntity<?> response = userController.updateAvatarFromAuthorizedUser(image, authentication);

        verify(userService).updateUserAvatar(image, authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetImage() {

        long id = 1L;
        Avatar avatar = mock(Avatar.class);
        byte[] imageData = new byte[10];

        when(avatarService.getAvatar(id)).thenReturn(avatar);
        when(avatar.getData()).thenReturn(imageData);

        ResponseEntity<byte[]> response = userController.getImage(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(imageData, response.getBody());
    }

}
