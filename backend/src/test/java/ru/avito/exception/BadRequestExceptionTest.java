package ru.avito.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BadRequestExceptionTest {

    @Test
    void shouldCreateException() {
        BadRequestException ex = new BadRequestException("error");
        assertEquals("error", ex.getMessage());
    }
}