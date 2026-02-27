package ru.skypro.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController

public class HomeworkApplication {

  // Простой тест
  @GetMapping("/")
  public String home() {
    return "Homework Application is running!";
  }

  @GetMapping("/ping")
  public String ping() {
    return "pong";
  }

  @GetMapping("/api/simple-test")
  public String simpleTest() {
    return "API test endpoint works!";
  }
  public static void main(String[] args) {
    SpringApplication.run(HomeworkApplication.class, args);
  }
}