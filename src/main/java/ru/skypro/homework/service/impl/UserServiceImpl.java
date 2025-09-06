package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Override public void setPassword(NewPassword dto) { }
    @Override public User getMe() { return new User(); }
    @Override public UpdateUser updateMe(UpdateUser dto) { return dto; }
    @Override public void updateUserImage(MultipartFile image) { }
}
