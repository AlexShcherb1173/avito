package ru.avito.service;

import org.springframework.web.multipart.MultipartFile;
import ru.avito.dto.auth.NewPasswordRequest;
import ru.avito.dto.user.UpdateUserImageResponse;
import ru.avito.dto.user.UpdateUserRequest;
import ru.avito.dto.user.UserDto;

public interface UserService {

    UserDto getCurrentUser();

    UserDto updateCurrentUser(UpdateUserRequest request);

    void updatePassword(NewPasswordRequest request);

    UpdateUserImageResponse updateUserImage(MultipartFile image);
}