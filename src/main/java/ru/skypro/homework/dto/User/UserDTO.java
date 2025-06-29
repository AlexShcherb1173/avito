package ru.skypro.homework.dto.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skypro.homework.dto.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о пользователе системы")
/**
 * Информация о пользователе
 */
public class UserDTO {

    @Schema(description = "ID пользователя")
    private Long id;

    @Schema(description = "Email пользователя")
    private String email = "";

    @Schema(description = "Имя пользователя")
    private String firstName = "";

    @Schema(description = "Фамилия пользователя")
    private String lastName = "";

    @Schema(description = "Пароль пользователя")
    public String password = "";

    @Schema(description = "Телефон в формате +7XXXYYYZZZZ")
    private String phone = "";

    @Schema(description = "Роль пользователя",
            allowableValues = {"USER", "ADMIN"})
    private Role role = Role.USER;

    @Schema(description = "Ссылка на аватар",
            example = "/users/image/123")
    private String image = "";

}
