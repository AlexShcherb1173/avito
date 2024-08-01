package ru.skypro.homework.dto.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NewPasswordDTO {
    @JsonProperty("currentPassword")
    private String currentPassword;
    @JsonProperty("newPassword")
    private String newPassword;
}
