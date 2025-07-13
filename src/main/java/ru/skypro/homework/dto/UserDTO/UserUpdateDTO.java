package ru.skypro.homework.dto.UserDTO;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO для обновления данных пользователя
 */
public class UserUpdateDTO {
    @Size(min = 3, max = 50, message = "Имя должно быть от 3 до 50 символов")
    private String username; // Новое имя пользователя

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Некорректный формат телефона")
    private String phone; // Новый номер телефона

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
