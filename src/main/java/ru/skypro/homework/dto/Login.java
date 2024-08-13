package ru.skypro.homework.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Data
public class Login {
    private String id;
    private String username;
    private String password;

    public Login(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Login login = (Login) o;
        return Objects.equals(id, login.id) && Objects.equals(username, login.username) && Objects.equals(password, login.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }
}