package ru.skypro.homework.dto.rq.user;

import lombok.Data;

@Data
public class UpdateUser {
    /**
     * Поля относящиеся к сущности UserEntity
     */
    private String lastName;
    private String phone;
    private String firstName;
}
