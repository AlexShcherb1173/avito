package ru.skypro.homework.service;

import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.User;

public interface UsersService {
    public String setPassword(NewPassword newPassword);
    public User getUser();
    public String updateUser();

};
