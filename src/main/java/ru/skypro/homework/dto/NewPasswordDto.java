package ru.skypro.homework.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class NewPasswordDto {

    private String currentPassword;
    private String newPassword;
}
