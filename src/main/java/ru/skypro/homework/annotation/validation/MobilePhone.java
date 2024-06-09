package ru.skypro.homework.annotation.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NotNull
//@Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
@Pattern(regexp = "\\+7\\s?(\\(\\d{3}\\)|\\d{3})\\s?(\\d{3}\\d{2}\\d{2}|\\d{3}-\\d{2}-\\d{2})")
@Constraint(validatedBy = {})
@ReportAsSingleViolation
@Documented
@Target({ElementType.FIELD})
@Retention(RUNTIME)
public @interface MobilePhone {
    String message() default "Номер телефона не соответствует формату";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
