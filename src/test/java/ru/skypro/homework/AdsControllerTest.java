package ru.skypro.homework;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.skypro.homework.controller.AdsController;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdsControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private AdsController controller;

    @Test
    void contextLoadsTest() {
        Assertions.assertThat(controller).isNotNull();
    }
}
