package ru.avito.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateUserWithBuilder() {
        User user = User.builder()
                .id(1)
                .email("test@mail.com")
                .password("pass")
                .firstName("John")
                .lastName("Doe")
                .phone("123")
                .role(Role.ADMIN)
                .image("img.png")
                .build();

        assertEquals(1, user.getId());
        assertEquals("test@mail.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        User u1 = User.builder().id(1).email("a@mail.com").build();
        User u2 = User.builder().id(1).email("a@mail.com").build();

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void shouldTestSetters() {
        User user = new User();
        user.setEmail("mail");
        user.setPassword("pass");

        assertEquals("mail", user.getEmail());
        assertEquals("pass", user.getPassword());
    }

    @Test
    void shouldBuildUserCorrectly() {
        User user = User.builder()
                .id(1)
                .email("user@example.com")
                .password("encoded")
                .firstName("Ivan")
                .lastName("Ivanov")
                .phone("+79990000001")
                .role(Role.USER)
                .image("/img.jpg")
                .build();

        assertEquals(1, user.getId());
        assertEquals("user@example.com", user.getEmail());
        assertEquals("encoded", user.getPassword());
        assertEquals("Ivan", user.getFirstName());
        assertEquals("Ivanov", user.getLastName());
        assertEquals("+79990000001", user.getPhone());
        assertEquals(Role.USER, user.getRole());
        assertEquals("/img.jpg", user.getImage());
    }

    @Test
    void shouldAllowSetters() {
        User user = new User();

        user.setId(2);
        user.setEmail("test@mail.com");
        user.setFirstName("Test");

        assertEquals(2, user.getId());
        assertEquals("test@mail.com", user.getEmail());
        assertEquals("Test", user.getFirstName());
    }
}