package ru.skypro.homework.serviceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.impl.ImageServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceImplTest {

    @Mock
    private ImageRepository repository;

    @InjectMocks
    private ImageServiceImpl imageService;

    private MultipartFile mockFile;

    @BeforeEach
    public void setUp() {
        // Создаем мок MultipartFile
        mockFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test data".getBytes());
    }

    @Test
    public void testUploadImage() throws IOException {
        long id = 1L;

        // Убедимся, что директория для загрузки существует или создается.
        Path uploadDirectory = Path.of(System.getProperty("user.dir"), "images");
        Files.createDirectories(uploadDirectory);

        // Вызов метода uploadImage
        Image uploadedImage = imageService.uploadImage(id, mockFile);

        // Проверяем, что возвращаемый объект не null
        assertNotNull(uploadedImage);
        assertEquals("test.jpg", uploadedImage.getFilePath().substring(uploadedImage.getFilePath().lastIndexOf("/") + 1));
        assertEquals(mockFile.getSize(), uploadedImage.getFileSize());
        assertEquals(mockFile.getContentType(), uploadedImage.getMediaType());

        // Проверяем, что метод save был вызван один раз
        verify(repository, times(1)).save(any(Image.class));

        // Удаляем созданные файлы после теста
        Files.deleteIfExists(Path.of(uploadDirectory.toString(), "listing_" + id + ".jpg"));
    }

    @Test
    public void testRemoveImage() {
        // подготовка данных
        Image image = mock(Image.class);

        // вызов метода removeImage
        imageService.removeImage(image);

        // проверка взаимодействия
        verify(repository).delete(image);
    }

    @Test
    public void testGetImage() {
        // подготовка данных
        Long id = 1L;
        Image image = mock(Image.class);
        when(repository.findById(id)).thenReturn(Optional.of(image));

        // вызов метода getImage
        Image result = imageService.getImage(id);

        // проверка результата
        assertEquals(image, result);
    }

    @Test
    public void testGetImageNotFound() {
        // подготовка данных
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // вызов метода getImage
        Image result = imageService.getImage(id);

        // проверка результата
        assertNotNull(result);
    }


}
