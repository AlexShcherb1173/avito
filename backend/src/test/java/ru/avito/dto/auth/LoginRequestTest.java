package ru.avito.dto.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldSetAndGetFields() {
        LoginRequest request = new LoginRequest();
        request.setUsername("user@example.com");
        request.setPassword("password");

        assertEquals("user@example.com", request.getUsername());
        assertEquals("password", request.getPassword());
    }

    @Test
    void shouldPassValidationForValidRequest() {
        LoginRequest request = new LoginRequest();
        request.setUsername("user@example.com");
        request.setPassword("password");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidationForInvalidFields() {
        LoginRequest request = new LoginRequest();
        request.setUsername("invalid-email");
        request.setPassword("");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Email has invalid format")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Password must not be blank")));
    }

    @Test
    void shouldSupportEqualsHashCodeAndToString() {
        LoginRequest first = new LoginRequest();
        first.setUsername("user@example.com");
        first.setPassword("password");

        LoginRequest second = new LoginRequest();
        second.setUsername("user@example.com");
        second.setPassword("password");

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertTrue(first.toString().contains("user@example.com"));
    }
}