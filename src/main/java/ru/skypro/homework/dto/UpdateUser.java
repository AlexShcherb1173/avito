package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.skypro.homework.annotation.validation.FirstName;
import ru.skypro.homework.annotation.validation.LastName;
import ru.skypro.homework.annotation.validation.MobilePhone;

/**
 * DTO для изменения пользователя
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUser {
    /**
     * имя пользователя
     */
    @FirstName
    @Schema(description = "имя пользователя")
    private String firstName;
    /**
     * фамилия пользователя
     */
    @LastName
    @Schema(description = "фамилия пользователя")
    private String lastName;
    /**
     * телефон пользователя
     */
    @Schema(description = "телефон пользователя")
    @MobilePhone
    private String phone;
}
