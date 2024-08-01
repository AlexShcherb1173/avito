package ru.skypro.homework.dto.autoAndReg;

/* Здесь должна быть хорошая авторизация. И будет. Наверно.*/

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Login {

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
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
