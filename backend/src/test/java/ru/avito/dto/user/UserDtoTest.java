package ru.avito.dto.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoTest {

    @Test
    void shouldUseConstructorsAndSetters() {
        UserDto first = new UserDto(1, "user@example.com", "Ivan", "Ivanov", "+79990000001", "USER", "/img.jpg");

        assertEquals(1, first.getId());
        assertEquals("user@example.com", first.getEmail());
        assertEquals("Ivan", first.getFirstName());
        assertEquals("Ivanov", first.getLastName());
        assertEquals("+79990000001", first.getPhone());
        assertEquals("USER", first.getRole());
        assertEquals("/img.jpg", first.getImage());

        UserDto second = new UserDto();
        second.setId(1);
        second.setEmail("user@example.com");
        second.setFirstName("Ivan");
        second.setLastName("Ivanov");
        second.setPhone("+79990000001");
        second.setRole("USER");
        second.setImage("/img.jpg");

        assertEquals(1, second.getId());
        assertEquals("user@example.com", second.getEmail());
        assertEquals("Ivan", second.getFirstName());
        assertEquals("Ivanov", second.getLastName());
        assertEquals("+79990000001", second.getPhone());
        assertEquals("USER", second.getRole());
        assertEquals("/img.jpg", second.getImage());
    }

    @Test
    void shouldSupportEqualsHashCodeAndToString() {
        UserDto first = new UserDto(1, "user@example.com", "Ivan", "Ivanov", "+79990000001", "USER", "/img.jpg");
        UserDto second = new UserDto(1, "user@example.com", "Ivan", "Ivanov", "+79990000001", "USER", "/img.jpg");

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertTrue(first.toString().contains("user@example.com"));
    }
}