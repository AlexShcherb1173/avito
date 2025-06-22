package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Роль пользователя
 */
@Schema(description = "Роль пользователя",
        allowableValues = {"USER", "ADMIN"},
        defaultValue = "USER")
public enum Role {
    USER, ADMIN
}
