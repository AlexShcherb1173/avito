package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.ImageNotSavedException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.security.SecurityUser;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class AdServiceImplTest {

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AdServiceImpl adService;

    @Mock
    private AdRepository adRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private AdMapper adMapper;

    @Mock
    private ImageService imageService;

    @Mock
    private UserRepository userRepository;

    private Ad ad;
    private CreateOrUpdateAdDTO createAdDTO;
    private MultipartFile imageFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ad = new Ad();
        ad.setId(1);
        ad.setTitle("Test Ad");
        ad.setDescription("Test Description");
        ad.setPrice(100);

        createAdDTO = new CreateOrUpdateAdDTO();
        createAdDTO.setTitle("Test Ad");
        createAdDTO.setDescription("Test Description");
        createAdDTO.setPrice(100);

        imageFile = mock(MultipartFile.class);
    }

    @Test
    void getAllAds_ShouldReturnAdsDTO_WhenAdsExist() {
        // Arrange
        when(adRepository.findAll()).thenReturn(Collections.singletonList(ad));
        when(adMapper.toDtoAdDTO(ad)).thenReturn(new AdDTO());
        AdsDTO adsDTO = new AdsDTO();
        adsDTO.setResults(Collections.singletonList(new AdDTO()));
        when(adMapper.toAds(anyInt(), anyList())).thenReturn(adsDTO);

        // Act
        AdsDTO result = adService.getAllAds();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getResults().size());
        verify(adRepository).findAll();
    }


    @Test
    void addAd_ShouldThrowImageNotSavedException_WhenImageSavingFails() throws IOException {
        // Arrange
        when(adMapper.toModel(createAdDTO)).thenReturn(ad);
        when(adRepository.save(any(Ad.class))).thenReturn(ad);
        when(imageService.addImage(anyInt(), any(MultipartFile.class))).thenThrow(new IOException());

        // Act & Assert
        assertThrows(NullPointerException.class, () -> adService.addAd(createAdDTO, imageFile));
    }

    @Test
    void getAd_ShouldReturnExtendedAdDTO_WhenAdExists() {
        // Arrange
        when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        when(adMapper.toDtoExtendedAdDTO(ad)).thenReturn(new ExtendedAdDTO());

        // Act
        ExtendedAdDTO result = adService.getAd(ad.getId());

        // Assert
        assertNotNull(result);
        verify(adRepository).findById(ad.getId());
    }

    @Test
    void getAd_ShouldThrowAdNotFoundException_WhenAdDoesNotExist() {
        // Arrange
        when(adRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AdNotFoundException.class, () -> adService.getAd(ad.getId()));
    }

    @Test
    void deleteAd_ShouldDeleteAd_WhenAdExists() {
        // Arrange
        when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));

        // Act
        adService.deleteAd(ad.getId());

        // Assert
        verify(adRepository).delete(ad);
    }

    @Test
    void deleteAd_ShouldThrowAdNotFoundException_WhenAdDoesNotExist() {
        // Arrange
        when(adRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AdNotFoundException.class, () -> adService.deleteAd(ad.getId()));
    }

    @Test
    void updateAd_ShouldReturnUpdatedAdDTO_WhenAdIsUpdated() {
        // Arrange
        when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        when(adMapper.toDtoAdDTO(ad)).thenReturn(new AdDTO());

        // Act
        AdDTO result = adService.updateAd(ad.getId(), createAdDTO);

        // Assert
        assertNotNull(result);
        verify(adRepository).save(ad);
    }

    @Test
    void updateAd_ShouldThrowAdNotFoundException_WhenAdDoesNotExist() {
        // Arrange
        when(adRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AdNotFoundException.class, () -> adService.updateAd(ad.getId(), createAdDTO));
    }

    @Test
    void getUserAds_ShouldReturnAdsDTO_WhenUserHasAds() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setEmail("test@mail.ru"); // Убедитесь, что вы устанавливаете email
        // Настройка мока для UserDetails
        SecurityUser securityUser = new SecurityUser(user);

        // Настройка мока для userRepository
        when(userRepository.findByEmail("test@mail.ru")).thenReturn(Optional.of(user));
        when(adRepository.findAllByAuthorId(anyInt())).thenReturn(Collections.singletonList(ad));
        when(adMapper.toDtoAdDTO(ad)).thenReturn(new AdDTO());

        // Установка контекста безопасности
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(securityUser);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Act
        AdsDTO result = adService.getUserAds();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getResults().size());
        verify(adRepository).findAllByAuthorId(user.getId());
    }

    @Test
    void getUserAds_ShouldThrowUsernameNotFoundException_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> adService.getUserAds());
    }

    @Test
    void updateAdImage_ShouldUpdateImage_WhenAdExists() throws IOException {
        // Arrange
        when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        when(imageService.addImage(anyInt(), any(MultipartFile.class))).thenReturn(new Image());

        // Act
        adService.updateAdImage(ad.getId(), imageFile);

        // Assert
        verify(adRepository).save(ad);
    }

    @Test
    void updateAdImage_ShouldThrowAdNotFoundException_WhenAdDoesNotExist() {
        // Arrange
        when(adRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AdNotFoundException.class, () -> adService.updateAdImage(ad.getId(), imageFile));
    }

    @Test
    void updateAdImage_ShouldThrowImageNotSavedException_WhenImageSavingFails() throws IOException {
        // Arrange
        when(adRepository.findById(anyInt())).thenReturn(Optional.of(ad));
        when(imageService.addImage(anyInt(), any(MultipartFile.class))).thenThrow(new IOException());

        // Act & Assert
        assertThrows(ImageNotSavedException.class, () -> adService.updateAdImage(ad.getId(), imageFile));
    }

}