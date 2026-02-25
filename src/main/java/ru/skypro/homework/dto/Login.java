package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Schema(description = "DTO для входа")
@Data
public class Login {

    @Schema(description = "логин (min 4, max 32)")
    private String username;

    @Schema(description = "пароль (min 8, max 16)")
    private String password;

}
