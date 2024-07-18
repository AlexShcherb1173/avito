package ru.skypro.homework.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UpdateUserDto {

    private String firstName;
    private String lastName;
    private String phone;
}
