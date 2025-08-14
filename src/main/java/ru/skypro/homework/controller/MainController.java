package ru.skypro.homework.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://localhost:3000/")
public class MainController {

    @GetMapping("/")
    public String home() {
        return "Доступные эндпоинты:<br>" +
                "- <a href='/api/ads'>/api/ads</a> - Объявления<br>" +
                "- <a href='/api/user/me'>/api/user/me</a> - Профиль пользователя<br>" +
                "- <a href='/swagger-ui.html'>/swagger-ui.html</a> - Документация API";
    }
}