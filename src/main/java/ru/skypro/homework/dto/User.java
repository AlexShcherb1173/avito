package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class User {
    /*
    id пользователя
     */
    private Integer id;
    /*
    логин пользователя
     */
    private String email;
    /*
    имя пользователя
     */
    private String firstName;
    /*
    фамилия пользователя
     */
    private String lastName;
    /*
    телефон пользователя
     */
    private String phone;
    /*
    телефон пользователя
     */
    private Role role;
    /*
    ссылка на аватар пользователя
     */
    private String image;

}
