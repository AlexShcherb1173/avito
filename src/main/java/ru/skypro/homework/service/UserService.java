package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.User;

import java.io.IOException;
import java.util.NoSuchElementException;

public interface UserService {
    User getMe();

    User getUser(String email);

    void updateImage(String email, MultipartFile file) throws IOException, NoSuchElementException;

    void createUser(Register register);

    void deleteUser(String username);

    void changePassword(NewPassword password);

    boolean userExists(String username);

    UpdateUser updateUser(UpdateUser user);
}
