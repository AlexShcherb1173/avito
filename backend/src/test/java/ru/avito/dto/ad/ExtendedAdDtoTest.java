package ru.avito.dto.ad;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExtendedAdDtoTest {

    @Test
    void shouldUseAllArgsConstructor() {
        ExtendedAdDto dto = new ExtendedAdDto(
                1,
                "Title",
                "Description",
                1000,
                "/img.jpg",
                2,
                "Ivan",
                "Ivanov",
                "user@example.com",
                "+79990000001"
        );

        assertEquals(1, dto.getPk());
        assertEquals("Title", dto.getTitle());
        assertEquals("Description", dto.getDescription());
        assertEquals(1000, dto.getPrice());
        assertEquals("/img.jpg", dto.getImage());
        assertEquals(2, dto.getAuthor());
        assertEquals("Ivan", dto.getAuthorFirstName());
        assertEquals("Ivanov", dto.getAuthorLastName());
        assertEquals("user@example.com", dto.getEmail());
        assertEquals("+79990000001", dto.getPhone());
    }

    @Test
    void shouldSupportEqualsHashCodeAndToString() {
        ExtendedAdDto first = new ExtendedAdDto(
                1, "Title", "Description", 1000, "/img.jpg",
                2, "Ivan", "Ivanov", "user@example.com", "+79990000001"
        );
        ExtendedAdDto second = new ExtendedAdDto(
                1, "Title", "Description", 1000, "/img.jpg",
                2, "Ivan", "Ivanov", "user@example.com", "+79990000001"
        );

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertTrue(first.toString().contains("Title"));
    }
}