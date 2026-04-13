package ru.avito.dto.ad;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdDtoTest {

    @Test
    void shouldUseAllArgsConstructor() {
        AdDto dto = new AdDto(1, 2, "Title", 1000, "/img.jpg");

        assertEquals(1, dto.getPk());
        assertEquals(2, dto.getAuthor());
        assertEquals("Title", dto.getTitle());
        assertEquals(1000, dto.getPrice());
        assertEquals("/img.jpg", dto.getImage());
    }

    @Test
    void shouldUseNoArgsConstructorAndSetters() {
        AdDto dto = new AdDto();
        dto.setPk(1);
        dto.setAuthor(2);
        dto.setTitle("Title");
        dto.setPrice(1000);
        dto.setImage("/img.jpg");

        assertEquals(1, dto.getPk());
        assertEquals(2, dto.getAuthor());
        assertEquals("Title", dto.getTitle());
        assertEquals(1000, dto.getPrice());
        assertEquals("/img.jpg", dto.getImage());
    }

    @Test
    void shouldSupportEqualsHashCodeAndToString() {
        AdDto first = new AdDto(1, 2, "Title", 1000, "/img.jpg");
        AdDto second = new AdDto(1, 2, "Title", 1000, "/img.jpg");

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertTrue(first.toString().contains("Title"));
    }
}