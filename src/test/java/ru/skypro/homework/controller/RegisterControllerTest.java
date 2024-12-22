package ru.skypro.homework.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CustomUserDetailsManager;

import static ru.skypro.homework.constant.StaticForTests.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegisterControllerTest {

    @LocalServerPort
    int port;
    String baseUrl;

    @Autowired
    TestRestTemplate restTemplate;

        @MockitoSpyBean
    UserRepository userRepository;

        @MockitoSpyBean
    CustomUserDetailsManager userService;

    @BeforeEach
    void init() {
        baseUrl = "http://localhost:" + port + "/";
    }

    @AfterEach
    void cleanDB() {
        userRepository.deleteAll();
    }

    @Test
    void register() {
        Assertions.assertEquals(userRepository.findAll().size(), 0);
        restTemplate.postForLocation(baseUrl + "register", new RegisterDto(USER_ADMIN_EMAIL, USER_ADMIN_PASSWORD, USER_ADMIN_FIRST_NAME, USER_ADMIN_LAST_NAME, USER_ADMIN_PHONE, USER_ADMIN_ROLE));
        User userFromDb = userRepository.findByUsername(USER_ADMIN_EMAIL).orElseThrow();
        Assertions.assertEquals(1, userRepository.findAll().size());
        Assertions.assertEquals(USER_ADMIN_EMAIL, userFromDb.getUsername());
        Assertions.assertEquals(USER_ADMIN_FIRST_NAME, userFromDb.getFirstName());
        Assertions.assertEquals(USER_ADMIN_LAST_NAME, userFromDb.getLastName());
        Assertions.assertEquals(USER_ADMIN_PHONE, userFromDb.getPhone());
        Assertions.assertEquals(USER_ADMIN_ROLE, userFromDb.getRole());
        Assertions.assertNotEquals(USER_ADMIN_PASSWORD, userFromDb.getPassword());
        Mockito.verify(userService, Mockito.times(1)).createUser(Mockito.any(RegisterDto.class));
        //Пытаемся сдлать дубликат...
        restTemplate.postForLocation(baseUrl + "register", new RegisterDto(USER_ADMIN_EMAIL, USER_USER1_PASSWORD, USER_USER1_FIRST_NAME, USER_USER1_LAST_NAME, USER_USER1_PHONE, USER_USER1_ROLE));
        Assertions.assertEquals(1, userRepository.findAll().size());
    }
}