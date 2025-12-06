package ru.skypro.homework.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.AdsDto;
import ru.skypro.homework.dto.ads.ExtendedAdDto;
import ru.skypro.homework.exception.EntityNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.mapper.CollectionMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.AdServiceImpl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdServiceImplTest {

    @Mock
    private AdRepository adRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdMapper adMapper;

    @Mock
    private CollectionMapper collectionMapper;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private AdServiceImpl adService;

    private UserEntity testUser;
    private AdEntity testAd;
    private AdDto testAdDto;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setId(1);
        testUser.setEmail("test@example.com");

        testAd = new AdEntity();
        testAd.setId(1);
        testAd.setTitle("Test Ad");
        testAd.setPrice(1000);
        testAd.setAuthor(testUser);

        testAdDto = new AdDto();
        testAdDto.setPk(1);
        testAdDto.setTitle("Test Ad");
        testAdDto.setPrice(1000);
        testAdDto.setAuthor(1);
    }

    @Test
    void getAllAds_ShouldReturnAdsDto() {
        // Given
        List<AdEntity> ads = List.of(testAd);
        AdsDto expectedDto = new AdsDto();
        expectedDto.setCount(1);
        expectedDto.setResults(List.of(testAdDto));

        when(adRepository.findAll()).thenReturn(ads);
        when(collectionMapper.toAdsDto(ads)).thenReturn(expectedDto);

        // When
        AdsDto result = adService.getAllAds();

        // Then
        assertNotNull(result);
        assertEquals(1, result.getCount());
        verify(adRepository).findAll();
        verify(collectionMapper).toAdsDto(ads);
    }

    @Test
    void getAd_WhenAdExists_ShouldReturnExtendedAdDto() {
        // Given
        Integer adId = 1;
        ExtendedAdDto expectedDto = new ExtendedAdDto();
        expectedDto.setPk(1);
        expectedDto.setTitle("Test Ad");

        when(adRepository.findById(adId)).thenReturn(Optional.of(testAd));
        when(adMapper.toExtendedAdDto(testAd)).thenReturn(expectedDto);

        // When
        ExtendedAdDto result = adService.getAd(adId);

        // Then
        assertNotNull(result);
        assertEquals(adId, result.getPk());
        verify(adRepository).findById(adId);
    }

    @Test
    void getAd_WhenAdNotExists_ShouldThrowException() {
        // Given
        Integer adId = 999;
        when(adRepository.findById(adId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> adService.getAd(adId));
        verify(adRepository).findById(adId);
    }

    @Test
    void deleteAd_WhenAdExists_ShouldDeleteAd() throws IOException {
        // Given
        Integer adId = 1;
        String username = "test@example.com";

        when(adRepository.findById(adId)).thenReturn(Optional.of(testAd));

        // When
        adService.deleteAd(adId, username);

        // Then
        verify(imageService).deleteImage(any(), any());
        verify(adRepository).delete(testAd);
    }

    @Test
    void isAdAuthor_WhenUserIsAuthor_ShouldReturnTrue() {
        // Given
        Integer adId = 1;
        String username = "test@example.com";

        when(adRepository.findById(adId)).thenReturn(Optional.of(testAd));
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(testUser));

        // When
        boolean result = adService.isAdAuthor(adId, username);

        // Then
        assertTrue(result);
    }

    @Test
    void isAdAuthor_WhenUserIsNotAuthor_ShouldReturnFalse() {
        // Given
        Integer adId = 1;
        String username = "other@example.com";

        UserEntity otherUser = new UserEntity();
        otherUser.setId(2);
        otherUser.setEmail("other@example.com");

        when(adRepository.findById(adId)).thenReturn(Optional.of(testAd));
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(otherUser));

        // When
        boolean result = adService.isAdAuthor(adId, username);

        // Then
        assertFalse(result);
    }
}