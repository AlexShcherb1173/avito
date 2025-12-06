package ru.skypro.homework.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.AdsDto;
import ru.skypro.homework.dto.ads.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ads.ExtendedAdDto;
import ru.skypro.homework.dto.user.Role;
import ru.skypro.homework.exception.EntityNotFoundException;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AdIntegrationTest {

    @Autowired
    private AdService adService;

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_TITLE = "Test Ad";
    private static final Integer TEST_PRICE = 1000;

    private UserEntity testUser;
    private AdEntity testAd;

    @BeforeEach
    void setUp() {
        // Очищаем базу перед каждым тестом
        adRepository.deleteAll();
        userRepository.deleteAll();

        // Создаем тестового пользователя
        testUser = new UserEntity();
        testUser.setEmail(TEST_EMAIL);
        testUser.setPassword("password");
        testUser.setFirstName("Иван");
        testUser.setLastName("Иванов");
        testUser.setPhone("89140001122");
        testUser.setRole(Role.USER);
        userRepository.save(testUser);

        // Создаем тестовое объявление
        testAd = new AdEntity();
        testAd.setTitle(TEST_TITLE);
        testAd.setPrice(TEST_PRICE);
        testAd.setDescription("Test Description");
        testAd.setAuthor(testUser);
        testAd.setImage("test_image.jpg");
        adRepository.save(testAd);
    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void getAllAds_ShouldReturnAllAds() {
        // When
        AdsDto result = adService.getAllAds();

        // Then
        assertNotNull(result);
        assertEquals(1, result.getCount());
        assertNotNull(result.getResults());
        assertEquals(TEST_TITLE, result.getResults().get(0).getTitle());
    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void getAd_WhenAdExists_ShouldReturnExtendedAdDto() {
        // When
        ExtendedAdDto result = adService.getAd(testAd.getId());

        // Then
        assertNotNull(result);
        assertEquals(testAd.getId(), result.getPk());
        assertEquals(TEST_TITLE, result.getTitle());
        assertEquals(TEST_PRICE, result.getPrice());
        assertEquals(TEST_EMAIL, result.getEmail());
    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void getAd_WhenAdNotExists_ShouldThrowException() {
        // When & Then
        assertThrows(EntityNotFoundException.class, () ->
                adService.getAd(999));
    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void getMyAds_ShouldReturnUserAds() {
        // When
        AdsDto result = adService.getMyAds(TEST_EMAIL);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getCount());
        assertEquals(TEST_TITLE, result.getResults().get(0).getTitle());
    }

    @Test
    @WithMockUser(username = TEST_EMAIL)
    void updateAd_ShouldUpdateAd() {
        // Given
        CreateOrUpdateAdDto updateDto = new CreateOrUpdateAdDto();
        updateDto.setTitle("Updated Title");
        updateDto.setPrice(2000);
        updateDto.setDescription("Updated Description");

        // When
        AdDto result = adService.updateAd(testAd.getId(), updateDto, TEST_EMAIL);

        // Then
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals(2000, result.getPrice());

        // Проверяем, что в базе обновилось
        AdEntity updatedAd = adRepository.findById(testAd.getId()).orElseThrow();
        assertEquals("Updated Title", updatedAd.getTitle());
        assertEquals(2000, updatedAd.getPrice().intValue());
    }
}