package ru.skypro.homework.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Backend is working! Time: " + java.time.LocalDateTime.now();
    }

    @GetMapping("/check")
    public java.util.Map<String, Object> check() {
        return java.util.Map.of(
                "status", "OK",
                "service", "Your Diploma Backend",
                "timestamp", java.time.LocalDateTime.now().toString(),
                "java", System.getProperty("java.version"),
                "port", "8080"
        );
    }
}