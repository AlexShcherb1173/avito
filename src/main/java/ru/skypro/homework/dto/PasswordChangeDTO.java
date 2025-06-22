package ru.skypro.homework.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PasswordChangeDTO {
    private String newPassword;
    private String currentPassword;
}
