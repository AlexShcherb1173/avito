package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void setPassword(NewPassword newPassword) {

    }

    @Override
    public User getUser() {
        return new User();
    }

    @Override
    public UpdateUser updateUser(UpdateUser updateUserDto) {
        if (updateUserDto == null) {
            return null;
        }
        return new UpdateUser();
    }

    @Override
    public void updateUserImage(User user, MultipartFile image) {

    }

}