package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Enumeration representing user roles in the system")
public enum Role {
    @Schema(description = "Regular user role")
    USER, ADMIN
}
