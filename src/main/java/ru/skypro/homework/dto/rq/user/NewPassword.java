package ru.skypro.homework.dto.rq.user;

import lombok.Data;

@Data
public class NewPassword {
   private String currentPassword;
   private String newPassword;
}
