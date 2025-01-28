package ru.skypro.homework.dto;

import lombok.Data;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;

@Data
public class UpdateUser {
    /*
    имя пользователя
     */
    @Size(min = 3, max = 10)
    private String firstName;
    /*
    фамилия пользователя
     */
    @Size(min = 3, max = 10)
    private String lastName;
    /*
    телефон пользователя
     */
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}\\-?\\d{2}\\-?\\d{2}")
    private String phone;
}
