package ru.skypro.homework.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class NewPassword {
    private final String currentPassword;
    private final String newPassword;
}
