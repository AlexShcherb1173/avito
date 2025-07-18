package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MainControllerTest {

    @Autowired
    private MainController mainController;

    @Test
    void home_ReturnsWelcomeMessage() {
        // Act
        String result = mainController.home();

        // Assert
        assertTrue(result.contains("Доступные эндпоинты"));
        assertTrue(result.contains("/api/ads"));
    }
}