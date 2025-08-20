package ru.skypro.homework.dto.request;
import jakarta.validation.constraints.Size;
import lombok.Data;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.Parameter;

@Data
public class SetPasswordRequest {
    @Size(min = 8,max = 16)
    private String currentPassword;
    @Size(min = 8, max = 16)
    private String newPassword;
}
