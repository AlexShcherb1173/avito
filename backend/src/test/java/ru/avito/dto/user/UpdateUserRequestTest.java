package ru.avito.dto.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UpdateUserRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldSetAndGetFields() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setFirstName("Ivan");
        request.setLastName("Ivanov");
        request.setPhone("+79990000001");

        assertEquals("Ivan", request.getFirstName());
        assertEquals("Ivanov", request.getLastName());
        assertEquals("+79990000001", request.getPhone());
    }

    @Test
    void shouldPassValidationForValidRequest() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setFirstName("Ivan");
        request.setLastName("Ivanov");
        request.setPhone("+79990000001");

        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidationForBlankFields() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setFirstName("");
        request.setLastName("");
        request.setPhone("");

        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(request);

        assertEquals(3, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("First name must not be blank")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Last name must not be blank")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Phone must not be blank")));
    }

    @Test
    void shouldSupportEqualsHashCodeAndToString() {
        UpdateUserRequest first = new UpdateUserRequest();
        first.setFirstName("Ivan");
        first.setLastName("Ivanov");
        first.setPhone("+79990000001");

        UpdateUserRequest second = new UpdateUserRequest();
        second.setFirstName("Ivan");
        second.setLastName("Ivanov");
        second.setPhone("+79990000001");

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertTrue(first.toString().contains("Ivan"));
    }
}