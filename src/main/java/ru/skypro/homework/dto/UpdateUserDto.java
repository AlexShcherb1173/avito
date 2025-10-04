package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object для обновления информации о пользователе.
 * Не включает изменение роли пользователя.
 */
@Schema(description = "User update data (without role change)")
public class UpdateUserDto {
    @Schema(description = "First name")
    private String firstName;

    @Schema(description = "Last name")
    private String lastName;

    @Schema(description = "Phone number")
    private String phone;

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}