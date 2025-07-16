package ru.skypro.homework.service;

import org.apache.catalina.User;
import ru.skypro.homework.dto.comment.UpdateUser;

public interface UserService {
    User getInformation();
    UpdateUser updateInformationAboutUser(UpdateUser updateUser);
    void UpdateImage(String image);
}
