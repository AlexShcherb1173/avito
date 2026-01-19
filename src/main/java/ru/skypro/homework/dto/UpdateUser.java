package ru.skypro.homework.dto;

import javax.validation.constraints.*;

public class UpdateUser {

    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов")
    private String firstName;

    @NotBlank(message = "Фамилия не может быть пустым")
    @Size(min = 2, max = 50, message = "Фамилия должна содержать от 2 до 50 символов")
    private String lastName;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Неверный формат телефона")
    private String phone;

    public UpdateUser() {}

    public UpdateUser(String firstName, String lastName, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}