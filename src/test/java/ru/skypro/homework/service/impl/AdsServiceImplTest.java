package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdsServiceImplTest {

    @Mock
    private AdRepository adRepository;

    @Mock
    private AdMapper adMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private AdsServiceImpl adsService;

    @Mock
    private ImageService imageService;

    @Test
    void getAllAds_ShouldReturnAds_WhenAdsExist() {
        AdEntity adEntity1 = new AdEntity();
        adEntity1.setId(1);
        AdEntity adEntity2 = new AdEntity();
        adEntity2.setId(2);
        List<AdEntity> adEntities = Arrays.asList(adEntity1, adEntity2);

        Ad ad1 = new Ad();
        ad1.setPk(1);
        Ad ad2 = new Ad();
        ad2.setPk(2);

        when(adRepository.findAll()).thenReturn(adEntities);
        when(adMapper.toDto(adEntity1)).thenReturn(ad1);
        when(adMapper.toDto(adEntity2)).thenReturn(ad2);

        Ads result = adsService.getAllAds();

        assertNotNull(result);
        assertEquals(2, result.getCount());
        assertEquals(2, result.getResults().size());
        verify(adRepository, times(1)).findAll();
    }

    @Test
    void getAllAds_ShouldReturnEmptyList_WhenNoAdsExist() {
        when(adRepository.findAll()).thenReturn(List.of());

        Ads result = adsService.getAllAds();

        assertNotNull(result);
        assertEquals(0, result.getCount());
        assertTrue(result.getResults().isEmpty());
    }

    @Test
    void getExtendedAd_ShouldReturnExtendedAd_WhenAdExists() {
        Integer adId = 1;
        AdEntity adEntity = new AdEntity();
        adEntity.setId(adId);
        ExtendedAd extendedAd = new ExtendedAd();
        extendedAd.setPk(adId);

        when(adRepository.findById(adId)).thenReturn(Optional.of(adEntity));
        when(adMapper.toExtendedAd(adEntity)).thenReturn(extendedAd);

        ExtendedAd result = adsService.getExtendedAd(adId);

        assertNotNull(result);
        assertEquals(adId, result.getPk());
        verify(adRepository, times(1)).findById(adId);
    }

    @Test
    void getExtendedAd_ShouldThrowException_WhenAdNotFound() {
        Integer adId = 999;
        when(adRepository.findById(adId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            adsService.getExtendedAd(adId);
        });
    }

    @Test
    void addAd_ShouldSaveAd_WithImage() throws IOException {
        String username = "user@example.com";
        CreateOrUpdateAd properties = new CreateOrUpdateAd();
        properties.setTitle("Test Ad");
        properties.setPrice(1000);
        properties.setDescription("Test Description");

        MultipartFile image = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "test image content".getBytes()
        );

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setEmail(username);

        AdEntity adEntity = new AdEntity();
        adEntity.setId(1);

        Ad expectedAd = new Ad();
        expectedAd.setPk(1);

        when(userService.getUserEntity(username)).thenReturn(userEntity);
        when(adMapper.toEntity(properties)).thenReturn(adEntity);
        when(adRepository.save(any(AdEntity.class))).thenReturn(adEntity);
        when(adMapper.toDto(adEntity)).thenReturn(expectedAd);

        Ad result = adsService.addAd(properties, image, username);

        assertNotNull(result);
        assertEquals(1, result.getPk());
        verify(userService, times(1)).getUserEntity(username);
        verify(adRepository, times(1)).save(any(AdEntity.class));
    }

    @Test
    void removeAd_ShouldDeleteAd_WhenUserIsOwner() {
        Integer adId = 1;
        String username = "owner@example.com";

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setRole(Role.USER);

        UserEntity adOwner = new UserEntity();
        adOwner.setId(1);

        AdEntity adEntity = new AdEntity();
        adEntity.setId(adId);
        adEntity.setAuthor(adOwner);

        when(userService.getUserEntity(username)).thenReturn(userEntity);
        when(adRepository.findById(adId)).thenReturn(Optional.of(adEntity));

        adsService.removeAd(adId, username);

        verify(adRepository, times(1)).delete(adEntity);
    }

    @Test
    void removeAd_ShouldThrowSecurityException_WhenUserIsNotOwner() {
        Integer adId = 1;
        String username = "notowner@example.com";

        UserEntity userEntity = new UserEntity();
        userEntity.setId(2);
        userEntity.setRole(Role.USER);

        UserEntity adOwner = new UserEntity();
        adOwner.setId(1);

        AdEntity adEntity = new AdEntity();
        adEntity.setId(adId);
        adEntity.setAuthor(adOwner);

        when(userService.getUserEntity(username)).thenReturn(userEntity);
        when(adRepository.findById(adId)).thenReturn(Optional.of(adEntity));

        assertThrows(SecurityException.class, () -> {
            adsService.removeAd(adId, username);
        });

        verify(adRepository, never()).delete(any());
    }

    @Test
    void getAdsByUser_ShouldReturnUserAds() {
        String username = "user@example.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);

        AdEntity adEntity = new AdEntity();
        adEntity.setId(1);
        List<AdEntity> userAds = List.of(adEntity);

        Ad ad = new Ad();
        ad.setPk(1);

        when(userService.getUserEntity(username)).thenReturn(userEntity);
        when(adRepository.findByAuthorId(1)).thenReturn(userAds);
        when(adMapper.toDto(adEntity)).thenReturn(ad);

        Ads result = adsService.getAdsByUser(username);

        assertNotNull(result);
        assertEquals(1, result.getCount());
        assertEquals(1, result.getResults().size());
        verify(adRepository, times(1)).findByAuthorId(1);
    }

    @Test
    void isAdOwner_ShouldReturnTrue_WhenUserIsOwner() {
        Integer adId = 1;
        String username = "owner@example.com";

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);

        UserEntity adOwner = new UserEntity();
        adOwner.setId(1);

        AdEntity adEntity = new AdEntity();
        adEntity.setId(adId);
        adEntity.setAuthor(adOwner);

        when(adRepository.findById(adId)).thenReturn(Optional.of(adEntity));
        when(userService.getUserEntity(username)).thenReturn(userEntity);

        boolean result = adsService.isAdOwner(adId, username);

        assertTrue(result);
    }

    @Test
    void isAdOwner_ShouldReturnFalse_WhenUserIsNotOwner() {
        Integer adId = 1;
        String username = "notowner@example.com";

        UserEntity userEntity = new UserEntity();
        userEntity.setId(2); // Different ID

        UserEntity adOwner = new UserEntity();
        adOwner.setId(1); // Different owner

        AdEntity adEntity = new AdEntity();
        adEntity.setId(adId);
        adEntity.setAuthor(adOwner);

        when(adRepository.findById(adId)).thenReturn(Optional.of(adEntity));
        when(userService.getUserEntity(username)).thenReturn(userEntity);

        boolean result = adsService.isAdOwner(adId, username);

        assertFalse(result);
    }

    @Test
    void isAdOwner_ShouldReturnFalse_WhenAdNotFound() {
        Integer adId = 999;
        String username = "user@example.com";

        when(adRepository.findById(adId)).thenReturn(Optional.empty());

        boolean result = adsService.isAdOwner(adId, username);

        assertFalse(result);
    }
}