package ru.skypro.homework.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
public class NewPasswordDto {

    @JsonProperty("currentPassword")
    @Size(min = 8, max = 16)
    private String currentPassword;

    @JsonProperty("newPassword")
    @Size(min = 8, max = 16)
    private String newPassword;
}
