package ru.skypro.homework.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserGettersAndSetters() {
        User user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhone("+79999999999");
        user.setRole(Role.USER);
        user.setImage("image.jpg");

        assertEquals(1, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("+79999999999", user.getPhone());
        assertEquals(Role.USER, user.getRole());
        assertEquals("image.jpg", user.getImage());
    }

    @Test
    void testUserEqualsAndHashCode() {
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("test@example.com");

        User user2 = new User();
        user2.setId(1);
        user2.setEmail("test@example.com");

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }
}