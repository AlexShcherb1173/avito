package ru.avito.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Resource not found";

        NotFoundException exception = new NotFoundException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldBeInstanceOfRuntimeException() {
        NotFoundException exception = new NotFoundException("error");

        assertTrue(exception instanceof RuntimeException);
    }
}