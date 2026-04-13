package ru.avito.dto.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.avito.entity.Role;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldSetAndGetFields() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("user@example.com");
        request.setPassword("password");
        request.setFirstName("Ivan");
        request.setLastName("Ivanov");
        request.setPhone("+79990000001");
        request.setRole(Role.USER);

        assertEquals("user@example.com", request.getUsername());
        assertEquals("password", request.getPassword());
        assertEquals("Ivan", request.getFirstName());
        assertEquals("Ivanov", request.getLastName());
        assertEquals("+79990000001", request.getPhone());
        assertEquals(Role.USER, request.getRole());
    }

    @Test
    void shouldPassValidationForValidRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("user@example.com");
        request.setPassword("password");
        request.setFirstName("Ivan");
        request.setLastName("Ivanov");
        request.setPhone("+79990000001");
        request.setRole(Role.USER);

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidationForInvalidFields() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("invalid-email");
        request.setPassword("12");
        request.setFirstName("");
        request.setLastName("");
        request.setPhone("");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertEquals(5, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Email has invalid format")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Password must contain at least 4 characters")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("First name must not be blank")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Last name must not be blank")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Phone must not be blank")));
    }

    @Test
    void shouldSupportEqualsHashCodeAndToString() {
        RegisterRequest first = new RegisterRequest();
        first.setUsername("user@example.com");
        first.setPassword("password");
        first.setFirstName("Ivan");
        first.setLastName("Ivanov");
        first.setPhone("+79990000001");
        first.setRole(Role.USER);

        RegisterRequest second = new RegisterRequest();
        second.setUsername("user@example.com");
        second.setPassword("password");
        second.setFirstName("Ivan");
        second.setLastName("Ivanov");
        second.setPhone("+79990000001");
        second.setRole(Role.USER);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertTrue(first.toString().contains("user@example.com"));
    }
}