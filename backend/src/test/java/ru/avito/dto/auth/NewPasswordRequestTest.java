package ru.avito.dto.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NewPasswordRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldSetAndGetFields() {
        NewPasswordRequest request = new NewPasswordRequest();
        request.setCurrentPassword("oldPass");
        request.setNewPassword("newPass");

        assertEquals("oldPass", request.getCurrentPassword());
        assertEquals("newPass", request.getNewPassword());
    }

    @Test
    void shouldPassValidationForValidRequest() {
        NewPasswordRequest request = new NewPasswordRequest();
        request.setCurrentPassword("oldPass");
        request.setNewPassword("newPass");

        Set<ConstraintViolation<NewPasswordRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidationForInvalidFields() {
        NewPasswordRequest request = new NewPasswordRequest();
        request.setCurrentPassword("");
        request.setNewPassword("12");

        Set<ConstraintViolation<NewPasswordRequest>> violations = validator.validate(request);

        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Current password must not be blank")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("New password must contain at least 4 characters")));
    }

    @Test
    void shouldSupportEqualsHashCodeAndToString() {
        NewPasswordRequest first = new NewPasswordRequest();
        first.setCurrentPassword("oldPass");
        first.setNewPassword("newPass");

        NewPasswordRequest second = new NewPasswordRequest();
        second.setCurrentPassword("oldPass");
        second.setNewPassword("newPass");

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertTrue(first.toString().contains("oldPass"));
    }
}