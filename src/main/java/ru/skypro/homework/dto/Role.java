package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Роли пользователей")
public enum Role {
    USER, ADMIN
}
