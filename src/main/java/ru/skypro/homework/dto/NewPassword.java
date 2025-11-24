package ru.skypro.homework.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor // создает конструктор
@NoArgsConstructor // создает пустой конструктор
public class NewPassword {
    private String currentPassword ; // Текущий пароль (по умолчанию пустая строка)
    private String newPassword ;     // Новый пароль (по умолчанию пустая строка)

}
