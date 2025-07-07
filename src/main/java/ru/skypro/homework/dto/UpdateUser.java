package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateUser {
    @JsonProperty("firstName")
    private String firstName;	// имя пользователя
    @JsonProperty("lastName")
    private String lastName;	// фамилия пользователя
    @JsonProperty("phone")
    private String phone;	// телефон пользователя
}
