package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class User {
    @JsonProperty("id")
    private long id;	// id пользователя
    @JsonProperty("email")
    private String email = "";	// логин пользователя
    @JsonProperty("firstName")
    private String firstName = "";	// имя пользователя
    @JsonProperty("lastName")
    private String lastName = "";	// фамилия пользователя
    @JsonProperty("phone")
    private String phone = "";	// телефон пользователя
    @JsonProperty("role")
    private String role = "USER";	// роль пользователя
    @JsonProperty("image")
    private String image = "";	// ссылка на аватар пользователя
}

