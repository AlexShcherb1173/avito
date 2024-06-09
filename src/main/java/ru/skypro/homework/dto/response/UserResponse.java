package ru.skypro.homework.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skypro.homework.dto.Role;

@Data
public class UserResponse {
    /**
     * id пользователя
     */
    @Schema(description = "id пользователя")
    private Long id;
    /**
     * логин пользователя
     */
    @Schema(description = "логин пользователя")
    private String email;
    /**
     * имя пользователя
     */
    @Schema(description = "имя пользователя")
    private String firstName;
    /**
     * фамилия пользователя
     */
    @Schema(description = "фамилия пользователя")
    private String lastName;
    /**
     * телефон пользователя
     */
    @Schema(description = "телефон пользователя")
    private String phone;
    /**
     * роль пользователя
     */
    @Schema(description = "роль пользователя")
    private Role role;
    /**
     * ссылка на аватар пользователя
     */
    @Schema(description = "ссылка на аватар пользователя")
    private String image;
}
