package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.entity.UserRole;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AdServiceTest {

    @Autowired private AdService adService;
    @Autowired private UserRepository userRepository;
    @Autowired private AdRepository adRepository;

    private UserEntity owner;
    private UserEntity otherUser;
    private UserEntity admin;
    private AdEntity ad;

    @BeforeEach
    void setUp() {
        adRepository.deleteAll();
        userRepository.deleteAll();

        owner = userRepository.save(UserEntity.builder()
                .email("owner@mail.com")
                .password("x")
                .firstName("Owner")
                .lastName("User")
                .role(UserRole.USER)
                .build());

        otherUser = userRepository.save(UserEntity.builder()
                .email("other@mail.com")
                .password("x")
                .firstName("Other")
                .lastName("User")
                .role(UserRole.USER)
                .build());

        admin = userRepository.save(UserEntity.builder()
                .email("admin@mail.com")
                .password("x")
                .firstName("Admin")
                .lastName("User")
                .role(UserRole.ADMIN)
                .build());

        ad = adRepository.save(AdEntity.builder()
                .title("Old title")
                .description("Old desc")
                .price(100)
                .author(owner)
                .image("old.png")
                .build());
    }

    @Test
    void updateAd_whenNotOwnerAndNotAdmin_shouldThrowForbidden() {
        CreateOrUpdateAd dto = new CreateOrUpdateAd();
        dto.setTitle("New title");
        dto.setDescription("New desc");
        dto.setPrice(999);

        assertThatThrownBy(() -> adService.updateAd(ad.getId(), otherUser.getEmail(), dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("403");
    }

    @Test
    void updateAd_whenAdmin_shouldAllow() {
        CreateOrUpdateAd dto = new CreateOrUpdateAd();
        dto.setTitle("Admin title");
        dto.setDescription("Admin desc");
        dto.setPrice(777);

        var updated = adService.updateAd(ad.getId(), admin.getEmail(), dto);

        assertThat(updated.getTitle()).isEqualTo("Admin title");
        assertThat(adRepository.findById(ad.getId())).isPresent();
    }

    @Test
    void deleteAd_whenOwner_shouldDelete() {
        adService.deleteAd(ad.getId(), owner.getEmail());
        assertThat(adRepository.findById(ad.getId())).isEmpty();
    }

    @Test
    void deleteAd_whenNotOwner_shouldThrowForbidden() {
        assertThatThrownBy(() -> adService.deleteAd(ad.getId(), otherUser.getEmail()))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("403");
        assertThat(adRepository.findById(ad.getId())).isPresent();
    }
}

