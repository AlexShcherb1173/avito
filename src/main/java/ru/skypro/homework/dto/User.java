package ru.skypro.homework.dto;

import lombok.Data;

import java.util.Objects;

@Data
public class User {

    private Integer id;         // id пользователя
    private String email;       // Логин пользователя
    private String firstName;   // Имя пользователя
    private String lastName;    // Фамилия пользователя
    private String phone;       // Телефон пользователя
    private Role role;         // Роль пользователя
    private String image;       // Ссылка на аватар пользователя

    public User() {
        this.id = null; // Значение по умолчанию для id
        this.email = ""; // Пустой email по умолчанию
        this.firstName = ""; // Пустое имя по умолчанию
        this.lastName = ""; // Пустая фамилия по умолчанию
        this.phone = ""; // Пустой телефон по умолчанию
        this.role = Role.USER; // Роль по умолчанию - USER
        this.image = ""; // Пустая ссылка на изображение по умолчанию
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(phone, user.phone) && role == user.role && Objects.equals(image, user.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, firstName, lastName, phone, role, image);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", role=" + role +
                ", image='" + image + '\'' +
                '}';
    }
}
