package ru.avito.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO ответа для операций аутентификации.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    /**
     * Признак успешного выполнения операции.
     */
    private boolean success;

    /**
     * Текстовое сообщение результата.
     */
    private String message;
}