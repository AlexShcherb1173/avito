package ru.skypro.homework.entity;

import org.junit.jupiter.api.Test;
import ru.skypro.homework.dto.Role;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {

    @Test
    void userEntity_ShouldCreateCorrectly() {
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhone("+79999999999");
        user.setRole(Role.USER);
        user.setImage("avatar.jpg");
        user.setPassword("encodedPassword");

        assertEquals(1, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("+79999999999", user.getPhone());
        assertEquals(Role.USER, user.getRole());
        assertEquals("avatar.jpg", user.getImage());
        assertEquals("encodedPassword", user.getPassword());
    }
}