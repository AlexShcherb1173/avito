package ru.avito.dto.ad;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageResponseTest {

    @Test
    void shouldUseConstructorsAndSetters() {
        ImageResponse first = new ImageResponse("/img.jpg");
        assertEquals("/img.jpg", first.getUrl());

        ImageResponse second = new ImageResponse();
        second.setUrl("/img2.jpg");
        assertEquals("/img2.jpg", second.getUrl());
    }

    @Test
    void shouldSupportEqualsHashCodeAndToString() {
        ImageResponse first = new ImageResponse("/img.jpg");
        ImageResponse second = new ImageResponse("/img.jpg");

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertTrue(first.toString().contains("/img.jpg"));
    }
}