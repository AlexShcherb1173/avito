package ru.skypro.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.dto.Role;

@SpringBootApplication
public class HomeworkApplication {


    public static void main(String[] args) {
        SpringApplication.run(HomeworkApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            if (userRepository.findByEmail("user@gmail.com").isEmpty()) {

                User user = new User();
                user.setEmail("user@gmail.com");
                user.setPassword(passwordEncoder.encode("123"));
                user.setFirstName("User");
                user.setLastName("User");
                user.setPhone("0000000000");
                user.setRole(Role.USER);

                userRepository.save(user);
            }

        };
    }


}
