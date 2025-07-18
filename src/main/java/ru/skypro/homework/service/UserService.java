package ru.skypro.homework.service;

import ru.skypro.homework.dto.User;
import ru.skypro.homework.dto.comment.UpdateUser;

public interface UserService {

    User getCurrentUserInfo();

    User updateUserInfo(UpdateUser updateUser);

    void setUserImage(String imageURL);

    boolean registerUser(ru.skypro.homework.dto.Register registerForm);
}