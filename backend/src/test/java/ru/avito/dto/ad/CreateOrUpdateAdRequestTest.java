package ru.avito.dto.ad;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CreateOrUpdateAdRequestTest {

    @Test
    void shouldSetAndGetFields() {
        CreateOrUpdateAdRequest request = new CreateOrUpdateAdRequest();
        request.setTitle("Valid title");
        request.setPrice(1000);
        request.setDescription("Valid description");

        assertEquals("Valid title", request.getTitle());
        assertEquals(1000, request.getPrice());
        assertEquals("Valid description", request.getDescription());
    }

    @Test
    void shouldPassValidationForValidRequest() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();

            CreateOrUpdateAdRequest request = new CreateOrUpdateAdRequest();
            request.setTitle("Valid title");
            request.setPrice(1000);
            request.setDescription("Valid description");

            Set<ConstraintViolation<CreateOrUpdateAdRequest>> violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }
    }

    @Test
    void shouldFailValidationWhenFieldsAreInvalid() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();

            CreateOrUpdateAdRequest request = new CreateOrUpdateAdRequest();
            request.setTitle("");
            request.setPrice(0);
            request.setDescription("");

            Set<ConstraintViolation<CreateOrUpdateAdRequest>> violations = validator.validate(request);

            assertEquals(5, violations.size());

            List<String> messages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .toList();

            assertTrue(messages.contains("Title must not be blank"));
            assertTrue(messages.contains("Title length must be between 8 and 32 characters"));
            assertTrue(messages.contains("Price must be greater than 0"));
            assertTrue(messages.contains("Description must not be blank"));
            assertTrue(messages.contains("Description length must be between 8 and 64 characters"));
        }
    }

    @Test
    void shouldFailValidationWhenTitleAndDescriptionAreTooShort() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();

            CreateOrUpdateAdRequest request = new CreateOrUpdateAdRequest();
            request.setTitle("short");
            request.setPrice(1000);
            request.setDescription("short");

            Set<ConstraintViolation<CreateOrUpdateAdRequest>> violations = validator.validate(request);

            assertEquals(2, violations.size());

            List<String> messages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .toList();

            assertTrue(messages.contains("Title length must be between 8 and 32 characters"));
            assertTrue(messages.contains("Description length must be between 8 and 64 characters"));
        }
    }
}