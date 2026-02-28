package ru.skypro.homework.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Schema(description = "DTO для смены пароля")
@Data
public class NewPassword {

    @Schema(description = "текущий пароль (min 8, max 16)")
    private String currentPassword;

    @Schema(description = "новый пароль (min 8, max 16)")
    private String newPassword;
}
