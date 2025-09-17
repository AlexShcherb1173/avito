package ru.skypro.homework.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skypro.homework.dto.Role;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Пользователь")
public class UserDto {

    @Schema(description = "Id пользователя")
    private long id;

    @Schema(description = "E-mail пользователя")
    private String email;

    @Schema(description = "Имя пользователя")
    private String firstName;

    @Schema(description = "Фамилия пользователя")
    private String lastName;

    @Schema(description = "Телефонный номер пользователя")
    private String phone;

    @Schema(description = "Роль пользователя")
    private Role role;

    @Schema(description = "Аватар пользователя")
    private String image;

}
