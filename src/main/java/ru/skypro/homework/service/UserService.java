package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDTO;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.model.User;

public interface UserService {
    UserDTO getUserDTO(User user);
    User createUserFromDTO(UserDTO userDTO);

    UserDTO updateUser(UpdateUserDTO updateUserDTO);

    UserDTO getCurrentUser();

    User getCurrentUserEntity();

    void updatePassword(NewPasswordDTO passwordDTO);

    UserDTO updateUserImage(MultipartFile image);
}
