package ru.avito.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileStorageExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "File error";

        FileStorageException exception = new FileStorageException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldCreateExceptionWithMessageAndCause() {
        String message = "File error";
        Throwable cause = new RuntimeException("IO problem");

        FileStorageException exception = new FileStorageException(message, cause);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldBeInstanceOfRuntimeException() {
        FileStorageException exception = new FileStorageException("error");

        assertTrue(exception instanceof RuntimeException);
    }
}