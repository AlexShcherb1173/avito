package ru.skypro.homework.annotation.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NotBlank(message = "Фамилия не может быть пустой")
@Size(min = 2, message = "Фамилия должна содержать минимум 2 символа")
@Size(max = 16, message = "Фамилия должна содержать максимум 16 символов")
@Constraint(validatedBy = {})
@Documented
@Target({ElementType.FIELD})
@Retention(RUNTIME)
public @interface LastName {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
