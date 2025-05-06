package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class Login {



    private String username;
    private String password;

    public Login(String password, String username) {
        this.password = password;
        this.username = username;
    }
    public Login(){}
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
