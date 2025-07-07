package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NewPassword {
    @JsonProperty("currentPassword")
    private String currentPassword;	// текущий пароль

    @JsonProperty("newPassword")
    private String newPassword;	// новый пароль
}
