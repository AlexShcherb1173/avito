package ru.avito.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PhoneValidatorTest {

    @Test
    void shouldCreateInstance() {
        PhoneValidator validator = new PhoneValidator();
        assertNotNull(validator);
    }
}