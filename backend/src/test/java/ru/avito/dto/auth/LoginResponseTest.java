package ru.avito.dto.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginResponseTest {

    @Test
    void shouldUseConstructorsAndSetters() {
        LoginResponse first = new LoginResponse(true, "Success");
        assertTrue(first.isSuccess());
        assertEquals("Success", first.getMessage());

        LoginResponse second = new LoginResponse();
        second.setSuccess(false);
        second.setMessage("Failed");

        assertFalse(second.isSuccess());
        assertEquals("Failed", second.getMessage());
    }

    @Test
    void shouldSupportEqualsHashCodeAndToString() {
        LoginResponse first = new LoginResponse(true, "Success");
        LoginResponse second = new LoginResponse(true, "Success");

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertTrue(first.toString().contains("Success"));
    }
}