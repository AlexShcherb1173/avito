package ru.skypro.homework.service;

import ru.skypro.homework.model.UpdateUser;
import ru.skypro.homework.model.User;

public interface UserService {

    User getCurrentUser();

    User updateUser(UpdateUser updateUser);
}
