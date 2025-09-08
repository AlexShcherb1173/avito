package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)

    @Schema(description = "id пользователя")
    protected Integer id;
    @Email
    @Schema(description = "логин пользователя")
    protected String email;
    @Schema(description = "имя пользователя")
    private String firstName;
    @Schema(description = "фамилия пользователя")
    private String lastName;
    @Schema(description = "телефон пользователя")
    private String phone;
    @Schema(description = "ссылка на аватар пользователя")
    private String image;

}
