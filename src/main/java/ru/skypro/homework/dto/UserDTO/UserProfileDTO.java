package ru.skypro.homework.dto.UserDTO;

import java.time.LocalDateTime;

/**
 * DTO для представления профиля пользователя
 */
public class UserProfileDTO {
    private Long id; // Уникальный идентификатор пользователя
    private String username; // Отображаемое имя
    private String email; // Email пользователя
    private String phone; // Номер телефона в формате +7...
    private String avatar; // URL аватарки
    private LocalDateTime registrationDate; // Дата регистрации

    public UserProfileDTO(Long id, String username, String email, String phone, String avatar, LocalDateTime registrationDate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
        this.registrationDate = registrationDate;
    }

    public UserProfileDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }
}
