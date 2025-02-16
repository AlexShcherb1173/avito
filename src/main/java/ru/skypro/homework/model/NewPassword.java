package ru.skypro.homework.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewPassword {
    String currentPassword;
    String newPassword;
}
