package ru.avito.dto.ad;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdsResponseTest {

    @Test
    void shouldUseAllArgsConstructor() {
        AdDto ad = new AdDto(1, 2, "Title", 1000, "/img.jpg");
        AdsResponse response = new AdsResponse(1, List.of(ad));

        assertEquals(1, response.getCount());
        assertEquals(1, response.getResults().size());
        assertEquals(ad, response.getResults().get(0));
    }

    @Test
    void shouldUseNoArgsConstructorAndSetters() {
        AdDto ad = new AdDto(1, 2, "Title", 1000, "/img.jpg");

        AdsResponse response = new AdsResponse();
        response.setCount(1);
        response.setResults(List.of(ad));

        assertEquals(1, response.getCount());
        assertEquals(1, response.getResults().size());
        assertEquals(ad, response.getResults().get(0));
    }

    @Test
    void shouldSupportEqualsHashCodeAndToString() {
        AdDto ad = new AdDto(1, 2, "Title", 1000, "/img.jpg");

        AdsResponse first = new AdsResponse(1, List.of(ad));
        AdsResponse second = new AdsResponse(1, List.of(ad));

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertTrue(first.toString().contains("count=1"));
    }
}