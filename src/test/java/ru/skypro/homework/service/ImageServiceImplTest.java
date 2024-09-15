package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.EmptyFileException;
import ru.skypro.homework.exception.ImageCanNotReadEception;
import ru.skypro.homework.exception.ImageNotFoundException;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.impl.ImageServiceImpl;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceImplTest {
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private ImageServiceImpl out;
    private Image testImage;
    private MultipartFile mockedFile;
    private Ad testAd;
    private User testUser;

    @BeforeEach
    void init() {
        testImage = new Image();
        testImage.setId(1L);
        testImage.setData(new byte[0]);
        testImage.setMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

        mockedFile = mock(MultipartFile.class);

        testUser = new User();
        testUser.setId(42L);
        testUser.setUsername("test@test.com");

        testAd = new Ad();
        testAd.setAuthor(testUser);

        testImage.setAd(testAd);
    }

    @Test
    void shouldThrowImageNotFoundException_whenGetAdsImageWithWrongId() {
        when(imageRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatExceptionOfType(ImageNotFoundException.class)
                .isThrownBy(() -> out.getAdImage(testImage.getId()));
    }

    @Test
    void shouldReturnBytesArray_whenGetAdsImage() {
        when(imageRepository.findById(anyLong())).thenReturn(Optional.of(testImage));

        byte[] result = out.getAdImage(testImage.getId());
        assertThat(result).isNotNull();
    }

    @Test
    void shouldReturnImageInstance_whenCreateImage() {
        when(imageRepository.save(any(Image.class))).thenReturn(testImage);

        Image result = out.createImage(mockedFile, new Ad());

        assertThat(result).isNotNull();
        verify(imageRepository, atMostOnce()).save(testImage);
    }

    @Test
    void shouldReturnBytesArray_whenUpdateAdsImage() throws IOException {
        Authentication auth = new UsernamePasswordAuthenticationToken(testUser, null);

        when(imageRepository.findById(anyLong())).thenReturn(Optional.of(testImage));
        when(mockedFile.getBytes()).thenReturn(new byte[0]);
        when(imageRepository.save(any(Image.class))).thenReturn(testImage);
        doNothing().when(userService).checkUserHasPermit(any(), anyString());

        byte[] result = out.updateAdImage(anyLong(), mockedFile, auth);

        assertThat(result).isNotNull();
        verify(imageRepository, atMostOnce()).save(testImage);
    }

    @Test
    void shouldThrowEmptyFileException_whenUseEmpty() {
        when(mockedFile.isEmpty()).thenReturn(true);

        assertThatExceptionOfType(EmptyFileException.class)
                .isThrownBy(() -> out.createImage(mockedFile, new Ad()));
    }

    @Test
    void shouldThrowImageCanNotBeReadException_whenUseBadFile() throws IOException {
        when(mockedFile.getBytes()).thenThrow(IOException.class);

        assertThatExceptionOfType(ImageCanNotReadEception.class)
                .isThrownBy(() -> out.createImage(mockedFile, new Ad()));
    }
}