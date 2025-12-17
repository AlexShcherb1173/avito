package ru.skypro.homework.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.skypro.homework.dto.user.Role;
import ru.skypro.homework.model.UserEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private static final String TEST_EMAIL = "test@example.com";

    @Test
    void findByEmail_ShouldReturnUser() {
        // Given
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(TEST_EMAIL);
        userEntity.setPassword("password");
        userEntity.setFirstName("Ivan");
        userEntity.setLastName("Ivanov");
        userEntity.setPhone("89140001122");
        userEntity.setRole(Role.USER);

        userRepository.save(userEntity);

        // When
        Optional<UserEntity> foundUser = userRepository.findByEmail(TEST_EMAIL);

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals(foundUser.get().getEmail(), TEST_EMAIL);
        assertEquals(foundUser.get().getFirstName(), "Ivan");
    }

    @Test
    void findByEmail_WhenUserNotFound_ShouldReturnEmpty(){
        // When
        Optional<UserEntity> foundUser = userRepository.findByEmail("nonexistent@example.com");

        // Then
        assertFalse(foundUser.isPresent());
    }
}
