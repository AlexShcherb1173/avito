package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.ads.Ads;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UsersRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AdsControllerTest {

    @Mock
    private AdRepository adRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AdsController adsController;

    @Test
    void getAllAds_EmptyList() {
        // Arrange
        when(adRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<Ads> response = adsController.getAllAds();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().getCount());
    }

    @Test
    void getAd_Exists() {
        // Arrange
        Ad ad = new Ad();
        ad.setId(1);
        when(adRepository.findById(1)).thenReturn(Optional.of(ad));

        // Act
        ResponseEntity<?> response = adsController.getAd(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}