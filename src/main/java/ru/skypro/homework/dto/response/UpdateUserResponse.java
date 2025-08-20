package ru.skypro.homework.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.parameters.ResponseEntity;
import io.swagger.v3.oas.annotations.Parameter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserResponse {

    private String firstName;
    private String lastName;
    private String phone;
}
