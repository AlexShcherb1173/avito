package ru.avito.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ValidationMessagesTest {

    @Test
    void shouldCreateInstance() {
        ValidationMessages messages = new ValidationMessages();
        assertNotNull(messages);
    }
}