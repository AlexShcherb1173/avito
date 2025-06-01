package ru.skypro.homework.dto;

import lombok.Data;

import java.util.Objects;

@Data
public class UpdateUser {

    private String firstName; // Имя пользователя
    private String lastName; // Фамилия пользователя
    private String phone; // Телефон пользователя

    public UpdateUser() {}

    public UpdateUser(String firstName, String lastName, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UpdateUser that = (UpdateUser) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, phone);
    }

    @Override
    public String toString() {
        return "UpdateUser{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
