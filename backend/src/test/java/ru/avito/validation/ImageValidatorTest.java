package ru.avito.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ImageValidatorTest {

    @Test
    void shouldCreateInstance() {
        ImageValidator validator = new ImageValidator();
        assertNotNull(validator);
    }
}