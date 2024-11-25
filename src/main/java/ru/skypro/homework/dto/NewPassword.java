package ru.skypro.homework.dto;

import lombok.*;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPassword {

    private String currentPassword;
    private String newPassword;

}
