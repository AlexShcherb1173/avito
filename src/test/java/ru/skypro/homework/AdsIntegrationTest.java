package ru.skypro.homework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.repository.UsersRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class AdsIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withUsername("student")
            .withPassword("chocolatefrog")
            .withDatabaseName("ads_online");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setup() {
        // Создаем тестовых пользователей
        Users admin = new Users();
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFirstName("Admin");
        admin.setLastName("Adminov");
        usersRepository.save(admin);

        Users regularUser = new Users();
        regularUser.setEmail("user@example.com");
        regularUser.setPassword(passwordEncoder.encode("user123"));

        regularUser.setFirstName("User");
        regularUser.setLastName("Userov");
        usersRepository.save(regularUser);
    }

    @Test
    void adminShouldDeleteAnyAd() {
        // 1. Аутентифицируемся как ADMIN
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin@example.com", "admin123");

        // 2. Создаем тестовое объявление (в реальном тесте можно использовать API или репозиторий)
        // Здесь предполагаем, что объявление с ID=1 уже существует

        // 3. Пытаемся удалить объявление
        ResponseEntity<Void> response = restTemplate.exchange(
                "/ads/1",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class);

        // 4. Проверяем успешное удаление
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void userCannotDeleteOthersAds() {
        // 1. Аутентифицируемся как обычный пользователь
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("user@example.com", "user123");

        // 2. Пытаемся удалить чужое объявление (ID=1)
        ResponseEntity<String> response = restTemplate.exchange(
                "/ads/1",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class);

        // 3. Проверяем ошибку доступа
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertTrue(response.getBody().contains("You are not the owner"));
    }

    @Test
    void userCanDeleteOwnAd() {
        // 1. Создаем объявление для пользователя (в реальном тесте через API)
        // Предположим, что объявление с ID=2 принадлежит user@example.com

        // 2. Аутентифицируемся как владелец
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("user@example.com", "user123");

        // 3. Пытаемся удалить свое объявление
        ResponseEntity<Void> response = restTemplate.exchange(
                "/ads/2",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class);

        // 4. Проверяем успешное удаление
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}