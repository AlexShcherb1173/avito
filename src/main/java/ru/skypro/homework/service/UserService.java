package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.generatedDto.UpdateUserDto;
import ru.skypro.homework.dto.generatedDto.UserDto;
import ru.skypro.homework.entity.User;

public interface UserService {
    UserDto getUserDtoByUsername(String username);

    User getUser(String username);

    UserDto getAuth();

    UserDto updateUser(UserDto userDto);

    void updateUserAvatar(String username, MultipartFile file);

    byte[] getUserAvatar(long id);

    void checkUserHasPermit(Authentication authentication, String username);
}
