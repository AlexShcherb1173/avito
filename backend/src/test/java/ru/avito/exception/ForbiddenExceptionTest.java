package ru.avito.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ForbiddenExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Access denied";

        ForbiddenException exception = new ForbiddenException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldBeInstanceOfRuntimeException() {
        ForbiddenException exception = new ForbiddenException("error");

        assertTrue(exception instanceof RuntimeException);
    }
}