package ru.skypro.homework.dto.authentication;

/* Здесь должна быть хорошая авторизация. И будет. Наверно.*/

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class LoginDto {
    // Dto для авторизации пользователя

    @JsonProperty("username") // имя пользователя
    private String username;

    @JsonProperty("password") // пароль
    @Size(min=8,max=16) // требование к длине
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
