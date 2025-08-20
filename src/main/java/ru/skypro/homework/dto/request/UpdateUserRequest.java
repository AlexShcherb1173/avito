package ru.skypro.homework.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.Parameter;

@Data
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    @Pattern(regexp = "^\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}$")
    private String phone;
}
