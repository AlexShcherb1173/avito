package ru.skypro.homework.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserUpdateDTO {
    private String firstName;
    private String lastName;
    private String phone;
}
