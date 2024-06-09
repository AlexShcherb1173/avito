package ru.skypro.homework.dto.user;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.skypro.homework.dto.NewPassword;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class NewPasswordTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory()
            .getValidator();

    @Test
    public void whenAllAcceptable() {
        NewPassword newPassword = NewPassword.builder()
                .currentPassword("01234567")
                .newPassword("0123456789123456")
                .build();

        Set<ConstraintViolation<NewPassword>> violations = validator.validate(newPassword);

        assertThat(violations).isEmpty();
    }


    @Nested
    class Invalid {

        @Test
        public void allNull() {
            NewPassword newPassword = NewPassword.builder()
                    .build();

            Set<ConstraintViolation<NewPassword>> violations = validator.validate(newPassword);

            assertThat(violations.size()).isEqualTo(2);
            assertThat(violations).anyMatch(
                    testObjectConstraintViolation ->
                            testObjectConstraintViolation.getPropertyPath().toString().equals("currentPassword") &&
                                    testObjectConstraintViolation.getMessage().contains("пуст"));
            assertThat(violations).anyMatch(
                    testObjectConstraintViolation ->
                            testObjectConstraintViolation.getPropertyPath().toString().equals("newPassword") &&
                                    testObjectConstraintViolation.getMessage().contains("пуст"));
        }

        @Test
        public void allLess() {
            NewPassword newPassword = NewPassword.builder()
                    .currentPassword("123456")
                    .newPassword("123456")
                    .build();

            Set<ConstraintViolation<NewPassword>> violations = validator.validate(newPassword);

            assertThat(violations.size()).isEqualTo(2);
            assertThat(violations).anyMatch(
                    testObjectConstraintViolation ->
                            testObjectConstraintViolation.getPropertyPath().toString().equals("currentPassword") &&
                                    testObjectConstraintViolation.getMessage().contains("8"));
            assertThat(violations).anyMatch(
                    testObjectConstraintViolation ->
                            testObjectConstraintViolation.getPropertyPath().toString().equals("newPassword") &&
                                    testObjectConstraintViolation.getMessage().contains("8"));
        }

        @Test
        public void allMore() {
            NewPassword newPassword = NewPassword.builder()
                    .currentPassword("01234567891234567")
                    .newPassword("01234567891234567")
                    .build();

            Set<ConstraintViolation<NewPassword>> violations = validator.validate(newPassword);

            assertThat(violations.size()).isEqualTo(2);
            assertThat(violations).anyMatch(
                    testObjectConstraintViolation ->
                            testObjectConstraintViolation.getPropertyPath().toString().equals("currentPassword") &&
                                    testObjectConstraintViolation.getMessage().contains("16"));
            assertThat(violations).anyMatch(
                    testObjectConstraintViolation ->
                            testObjectConstraintViolation.getPropertyPath().toString().equals("newPassword") &&
                                    testObjectConstraintViolation.getMessage().contains("16"));
        }
    }


}