package ru.skypro.homework.serviceImpl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Avatar;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.AvatarRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.AvatarServiceImpl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AvatarServiceImplTest {

    @Mock
    private AvatarRepository avatarRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AvatarServiceImpl avatarService;

    @Mock
    private MultipartFile multipartFile;

    @BeforeEach
    public void setUp() {
        // Настройка контекста безопасности для тестов
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));
    }

    @Test
    public void testUploadAvatar() throws IOException {
        // Arrange
        User user = new User();
        user.setId(1L);
        when(userRepository.findUserByEmailIgnoreCase("test@example.com")).thenReturn(Optional.of(user));

        when(multipartFile.getOriginalFilename()).thenReturn("avatar.png");
        when(multipartFile.getSize()).thenReturn(1024L);
        when(multipartFile.getContentType()).thenReturn("image/png");
        when(multipartFile.getBytes()).thenReturn(new byte[1024]);

        // Act
        Avatar avatar = avatarService.uploadAvatar(multipartFile);

        // Assert
        verify(avatarRepository, times(1)).save(any(Avatar.class));
        assertEquals("user_1.png", Path.of(avatar.getFilePath()).getFileName().toString());
        assertEquals(1024L, avatar.getFileSize());
        assertEquals("image/png", avatar.getMediaType());
    }

    @Test
    public void testRemoveAvatar() {
        // Arrange
        Avatar avatar = new Avatar();
        avatar.setId(1L);

        // Act
        avatarService.removeAvatar(avatar);

        // Assert
        verify(avatarRepository, times(1)).delete(avatar);
    }

    @Test
    public void testGetAvatar() {
        // Arrange
        Avatar avatar = new Avatar();
        avatar.setId(1L);
        when(avatarRepository.findById(1L)).thenReturn(Optional.of(avatar));

        // Act
        Avatar foundAvatar = avatarService.getAvatar(1L);

        // Assert
        assertNotNull(foundAvatar);
        assertEquals(1L, foundAvatar.getId());
    }

    @Test
    public void testGetAvatar_NotFound() {
        // Arrange
        when(avatarRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Avatar foundAvatar = avatarService.getAvatar(1L);

        // Assert
        assertNotNull(foundAvatar);
        assertEquals(0L, foundAvatar.getId()); // Проверка на пустой объект
    }
}
