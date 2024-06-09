package ru.skypro.homework.dto.user;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterDtoTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory()
            .getValidator();

    @Test
    public void whenAllAcceptable() {
        Register registerDto = Register.builder()
                .username("login@a.r")
                .password("Password")
                .firstName("Иван")
                .lastName("Иванов")
                .phone("+7 (123) 456-78-90")
                .role(Role.USER)
                .build();

        Set<ConstraintViolation<Register>> violations = validator.validate(registerDto);

        assertThat(violations).isEmpty();
    }

    @Nested
    class Invalid {

        @Test
        public void allNull() {
            Register registerDto = new Register();

            Set<ConstraintViolation<Register>> violations = validator.validate(registerDto);

            assertThat(violations.size()).isEqualTo(6);
            assertThat(violations).anyMatch(
                    testObjectConstraintViolation ->
                            testObjectConstraintViolation.getPropertyPath().toString().equals("username") &&
                                    testObjectConstraintViolation.getMessage().contains("пуст") &&
                                    testObjectConstraintViolation.getMessage().toLowerCase().contains("логин")
            );
            assertThat(violations).anyMatch(
                    testObjectConstraintViolation ->
                            testObjectConstraintViolation.getPropertyPath().toString().equals("password") &&
                                    testObjectConstraintViolation.getMessage().contains("пуст") &&
                                    testObjectConstraintViolation.getMessage().toLowerCase().contains("пароль")
            );
            assertThat(violations).anyMatch(
                    testObjectConstraintViolation ->
                            testObjectConstraintViolation.getPropertyPath().toString().equals("firstName") &&
                                    testObjectConstraintViolation.getMessage().contains("пуст") &&
                                    testObjectConstraintViolation.getMessage().toLowerCase().contains("имя")
            );
            assertThat(violations).anyMatch(
                    testObjectConstraintViolation ->
                            testObjectConstraintViolation.getPropertyPath().toString().equals("lastName") &&
                                    testObjectConstraintViolation.getMessage().contains("пуст") &&
                                    testObjectConstraintViolation.getMessage().toLowerCase().contains("фамилия")
            );
            assertThat(violations).anyMatch(
                    testObjectConstraintViolation ->
                            testObjectConstraintViolation.getPropertyPath().toString().equals("phone") &&
                                    testObjectConstraintViolation.getMessage().toLowerCase().contains("телефон")
            );
            assertThat(violations).anyMatch(
                    testObjectConstraintViolation ->
                            testObjectConstraintViolation.getPropertyPath().toString().equals("role") &&
                                    testObjectConstraintViolation.getMessage().contains("Неверная роль")
            );
        }

    }

}