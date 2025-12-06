package ru.skypro.homework.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.config.FileStorageConfig;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ads.ExtendedAdDto;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdMapperTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private FileStorageConfig fileStorageConfig;

    private AdMapper adMapper;

    private UserEntity testUser;
    private AdEntity testAd;
    private CreateOrUpdateAdDto testCreateDto;

    @BeforeEach
    void setUp() {
        adMapper = new AdMapper(userMapper, fileStorageConfig);

        testUser = new UserEntity();
        testUser.setId(1);
        testUser.setEmail("test@example.com");
        testUser.setFirstName("Иван");
        testUser.setLastName("Иванов");
        testUser.setPhone("+79140001122");

        testAd = new AdEntity();
        testAd.setId(1);
        testAd.setTitle("Test Ad");
        testAd.setPrice(1000);
        testAd.setDescription("Test Description");
        testAd.setAuthor(testUser);
        testAd.setImage("ad_image_test.jpg");

        testCreateDto = new CreateOrUpdateAdDto();
        testCreateDto.setTitle("New Ad");
        testCreateDto.setPrice(2000);
        testCreateDto.setDescription("New Description");

        // when(fileStorageConfig.getBaseUrl()).thenReturn("http://localhost:8080");
    }

    @Test
    void toDto_WhenEntityNotNull_ShouldReturnAdDto() {
        // Given
        when(fileStorageConfig.getBaseUrl()).thenReturn("http://localhost:8080");

        // When
        AdDto result = adMapper.toDto(testAd);

        // Then
        assertNotNull(result);
        assertEquals(testAd.getId(), result.getPk());
        assertEquals(testAd.getTitle(), result.getTitle());
        assertEquals(testAd.getPrice(), result.getPrice());
        assertEquals(testUser.getId(), result.getAuthor());
        assertEquals("http://localhost:8080/ads/image/1", result.getImage());

        verify(fileStorageConfig, times(1)).getBaseUrl();
    }

    @Test
    void toDto_WhenEntityNull_ShouldReturnNull() {
        // When
        AdDto result = adMapper.toDto(null);

        // Then
        assertNull(result);
        verifyNoInteractions(fileStorageConfig);
    }

    @Test
    void toDto_WhenImageIsNull_ShouldSetImageToNull() {
        // Given
        testAd.setImage(null);

        // When
        AdDto result = adMapper.toDto(testAd);

        // Then
        assertNotNull(result);
        assertNull(result.getImage());
        verifyNoInteractions(fileStorageConfig);
    }

    @Test
    void toDto_WhenImageIsEmpty_ShouldSetImageToNull() {
        // Given
        testAd.setImage("");

        // When
        AdDto result = adMapper.toDto(testAd);

        // Then
        assertNotNull(result);
        assertNull(result.getImage());
    }

    @Test
    void toExtendedAdDto_ShouldReturnExtendedAdDto() {
        // Given
        when(fileStorageConfig.getBaseUrl()).thenReturn("http://localhost:8080");

        // When
        ExtendedAdDto result = adMapper.toExtendedAdDto(testAd);

        // Then
        assertNotNull(result);
        assertEquals(testAd.getId(), result.getPk());
        assertEquals(testAd.getTitle(), result.getTitle());
        assertEquals(testAd.getPrice(), result.getPrice());
        assertEquals(testAd.getDescription(), result.getDescription());
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(testUser.getFirstName(), result.getAuthorFirstName());
        assertEquals(testUser.getLastName(), result.getAuthorLastName());
        assertEquals(testUser.getPhone(), result.getPhone());
        assertEquals("http://localhost:8080/ads/image/1", result.getImage());

        verify(fileStorageConfig, times(1)).getBaseUrl();
    }

    @Test
    void toExtendedAdDto_WhenEntityNull_ShouldReturnNull() {
        // When
        ExtendedAdDto result = adMapper.toExtendedAdDto(null);

        // Then
        assertNull(result);
    }

    @Test
    void toEntity_ShouldReturnAdEntity() {
        // When
        AdEntity result = adMapper.toEntity(testCreateDto);

        // Then
        assertNotNull(result);
        assertEquals(testCreateDto.getTitle(), result.getTitle());
        assertEquals(testCreateDto.getPrice(), result.getPrice());
        assertEquals(testCreateDto.getDescription(), result.getDescription());
        assertNull(result.getAuthor()); // Автор не устанавливается в этом методе
        assertNull(result.getImage()); // Изображение не устанавливается в этом методе
    }

    @Test
    void toEntity_WhenDtoNull_ShouldReturnNull() {
        // When
        AdEntity result = adMapper.toEntity(null);

        // Then
        assertNull(result);
    }

    @Test
    void updateEntityFromDto_ShouldUpdateEntityFields() {
        // Given
        AdEntity entity = new AdEntity();
        entity.setTitle("Old Title");
        entity.setPrice(500);
        entity.setDescription("Old Description");

        // When
        adMapper.updateEntityFromDto(entity, testCreateDto);

        // Then
        assertEquals(testCreateDto.getTitle(), entity.getTitle());
        assertEquals(testCreateDto.getPrice(), entity.getPrice());
        assertEquals(testCreateDto.getDescription(), entity.getDescription());
    }

    @Test
    void updateEntityFromDto_WhenDtoNull_ShouldNotUpdate() {
        // Given
        AdEntity entity = new AdEntity();
        entity.setTitle("Old Title");
        entity.setPrice(500);

        // When
        adMapper.updateEntityFromDto(entity, null);

        // Then
        assertEquals("Old Title", entity.getTitle());
        assertEquals(500, entity.getPrice());
    }

    @Test
    void updateEntityFromDto_WhenEntityNull_ShouldNotThrow() {
        // Should not throw exception
        adMapper.updateEntityFromDto(null, testCreateDto);
    }

    @Test
    void updateEntityFromDto_WhenDtoHasNullFields_ShouldNotUpdateThoseFields() {
        // Given
        AdEntity entity = new AdEntity();
        entity.setTitle("Old Title");
        entity.setPrice(500);
        entity.setDescription("Old Description");

        CreateOrUpdateAdDto partialDto = new CreateOrUpdateAdDto();
        partialDto.setTitle("New Title");
        // price и description остаются null

        // When
        adMapper.updateEntityFromDto(entity, partialDto);

        // Then
        assertEquals("New Title", entity.getTitle());
        assertEquals(500, entity.getPrice()); // Осталось прежним
        assertEquals("Old Description", entity.getDescription()); // Осталось прежним
    }
}