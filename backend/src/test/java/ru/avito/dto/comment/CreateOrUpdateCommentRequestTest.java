package ru.avito.dto.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CreateOrUpdateCommentRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldSetAndGetFields() {
        CreateOrUpdateCommentRequest request = new CreateOrUpdateCommentRequest();
        request.setText("Valid text");

        assertEquals("Valid text", request.getText());
    }

    @Test
    void shouldPassValidationForValidRequest() {
        CreateOrUpdateCommentRequest request = new CreateOrUpdateCommentRequest();
        request.setText("Valid text");

        Set<ConstraintViolation<CreateOrUpdateCommentRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidationForBlankText() {
        CreateOrUpdateCommentRequest request = new CreateOrUpdateCommentRequest();
        request.setText("");

        Set<ConstraintViolation<CreateOrUpdateCommentRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Comment text must not be blank")));
    }

    @Test
    void shouldFailValidationForTooShortText() {
        CreateOrUpdateCommentRequest request = new CreateOrUpdateCommentRequest();
        request.setText("short");

        Set<ConstraintViolation<CreateOrUpdateCommentRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Comment length must be between 8 and 64 characters")));
    }
}