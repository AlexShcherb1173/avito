package ru.skypro.homework.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.skypro.homework.dto.user.Role;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AdRepositoryTest {

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity testUser;
    private UserEntity anotherUser;

    @BeforeEach
    void setUp() {
        adRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new UserEntity();
        testUser.setEmail("user1@example.com");
        testUser.setPassword("password");
        testUser.setFirstName("Иван");
        testUser.setLastName("Иванов");
        testUser.setRole(Role.USER);
        userRepository.save(testUser);

        anotherUser = new UserEntity();
        anotherUser.setEmail("user2@example.com");
        anotherUser.setPassword("password");
        anotherUser.setFirstName("Петр");
        anotherUser.setLastName("Петров");
        anotherUser.setRole(Role.USER);
        userRepository.save(anotherUser);
    }

    @Test
    void saveAd_ShouldPersistAd() {
        // Given
        AdEntity ad = new AdEntity();
        ad.setTitle("Test Ad");
        ad.setPrice(1000);
        ad.setDescription("Test Description");
        ad.setAuthor(testUser);
        ad.setImage("ad_image_test.jpeg");

        // When
        AdEntity savedAd = adRepository.save(ad);

        // Then
        assertNotNull(savedAd.getId());
        assertEquals("Test Ad", savedAd.getTitle());
        assertEquals(1000, savedAd.getPrice());
        assertEquals(testUser, savedAd.getAuthor());
        assertTrue(adRepository.existsById(savedAd.getId()));
    }

    @Test
    void findById_WhenAdExists_ShouldReturnAd() {
        // Given
        AdEntity ad = new AdEntity();
        ad.setTitle("Test Ad");
        ad.setPrice(1000);
        ad.setDescription("Test Description");
        ad.setAuthor(testUser);
        ad.setImage("ad_image_test.jpeg");
        AdEntity savedAd = adRepository.save(ad);

        // When
        AdEntity foundAd = adRepository.findById(savedAd.getId()).orElse(null);

        // Then
        assertNotNull(foundAd);
        assertEquals(savedAd.getId(), foundAd.getId());
        assertEquals("Test Ad", foundAd.getTitle());
        assertEquals(testUser, foundAd.getAuthor());
    }

    @Test
    void findById_WhenAdNotExists_ShouldReturnEmpty() {
        // When
        var result = adRepository.findById(999);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void findByAuthorId_ShouldReturnOnlyUserAds() {
        // Given
        AdEntity ad1 = new AdEntity();
        ad1.setTitle("Ad 1");
        ad1.setPrice(1000);
        ad1.setAuthor(testUser);
        ad1.setDescription("Test Description2");
        ad1.setImage("ad_image_test1.jpeg");
        adRepository.save(ad1);

        AdEntity ad2 = new AdEntity();
        ad2.setTitle("Ad 2");
        ad2.setPrice(2000);
        ad2.setAuthor(testUser);
        ad2.setDescription("Test Description2");
        ad2.setImage("ad_image_test2.jpeg");
        adRepository.save(ad2);

        AdEntity ad3 = new AdEntity();
        ad3.setTitle("Ad 3");
        ad3.setPrice(3000);
        ad3.setAuthor(anotherUser);
        ad3.setDescription("Test Description3");
        ad3.setImage("ad_image_test3.jpeg");
        adRepository.save(ad3);

        // When
        List<AdEntity> userAds = adRepository.findByAuthorId(testUser.getId());

        // Then
        assertEquals(2, userAds.size());
        assertTrue(userAds.stream().allMatch(ad -> ad.getAuthor().getId().equals(testUser.getId())));
        assertTrue(userAds.stream().anyMatch(ad -> ad.getTitle().equals("Ad 1")));
        assertTrue(userAds.stream().anyMatch(ad -> ad.getTitle().equals("Ad 2")));
    }

    @Test
    void findByAuthorId_WhenUserHasNoAds_ShouldReturnEmptyList() {
        // Given - другой пользователь создал объявление
        AdEntity ad = new AdEntity();
        ad.setTitle("Other Ad");
        ad.setPrice(1000);
        ad.setAuthor(anotherUser);
        ad.setDescription("Test Description");
        ad.setImage("ad_image_test.jpeg");
        adRepository.save(ad);

        // When
        List<AdEntity> userAds = adRepository.findByAuthorId(testUser.getId());

        // Then
        assertNotNull(userAds);
        assertTrue(userAds.isEmpty());
    }

    @Test
    void findAll_ShouldReturnAllAds() {
        // Given
        AdEntity ad1 = new AdEntity();
        ad1.setTitle("Ad 1");
        ad1.setPrice(1000);
        ad1.setAuthor(testUser);
        ad1.setDescription("Test Description1");
        ad1.setImage("ad_image_test1.jpeg");
        adRepository.save(ad1);

        AdEntity ad2 = new AdEntity();
        ad2.setTitle("Ad 2");
        ad2.setPrice(2000);
        ad2.setAuthor(anotherUser);
        ad2.setDescription("Test Description2");
        ad2.setImage("ad_image_test2.jpeg");
        adRepository.save(ad2);

        // When
        List<AdEntity> allAds = adRepository.findAll();

        // Then
        assertEquals(2, allAds.size());
    }

    @Test
    void deleteById_ShouldRemoveAd() {
        // Given
        AdEntity ad = new AdEntity();
        ad.setTitle("Test Ad");
        ad.setPrice(1000);
        ad.setAuthor(testUser);
        ad.setDescription("Test Description");
        ad.setImage("ad_image_test.jpeg");
        AdEntity savedAd = adRepository.save(ad);

        // When
        adRepository.deleteById(savedAd.getId());

        // Then
        assertFalse(adRepository.existsById(savedAd.getId()));
    }

    @Test
    void existsById_WhenAdExists_ShouldReturnTrue() {
        // Given
        AdEntity ad = new AdEntity();
        ad.setTitle("Test Ad");
        ad.setPrice(1000);
        ad.setAuthor(testUser);
        ad.setDescription("Test Description");
        ad.setImage("ad_image_test.jpeg");
        AdEntity savedAd = adRepository.save(ad);

        // When & Then
        assertTrue(adRepository.existsById(savedAd.getId()));
    }

    @Test
    void existsById_WhenAdNotExists_ShouldReturnFalse() {
        // When & Then
        assertFalse(adRepository.existsById(999));
    }
}
