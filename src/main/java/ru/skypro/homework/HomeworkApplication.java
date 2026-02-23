package ru.skypro.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
        (
                exclude = {
                        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class
                }
        )
public class HomeworkApplication {
  public static void main(String[] args) {
    SpringApplication.run(HomeworkApplication.class, args);
  }
}
