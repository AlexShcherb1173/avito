package ru.avito.dto.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateUserImageResponseTest {

    @Test
    void shouldUseConstructorsAndSetters() {
        UpdateUserImageResponse first = new UpdateUserImageResponse("/img.jpg");
        assertEquals("/img.jpg", first.getImage());

        UpdateUserImageResponse second = new UpdateUserImageResponse();
        second.setImage("/img2.jpg");
        assertEquals("/img2.jpg", second.getImage());
    }

    @Test
    void shouldSupportEqualsHashCodeAndToString() {
        UpdateUserImageResponse first = new UpdateUserImageResponse("/img.jpg");
        UpdateUserImageResponse second = new UpdateUserImageResponse("/img.jpg");

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertTrue(first.toString().contains("/img.jpg"));
    }
}