package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UserModel;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImagesService;
import ru.skypro.homework.service.UsersService;

import java.io.IOException;

@Service
public class UsersServiceImpl implements UsersService {

    @Value("${ads.image-file-dir}")
    private String imageFileDir;
    private final ImagesService imagesService;
    private final UserRepository userRepository;
    private final UserDetailsManager  userDetailsManager;
    private final UserMapper userMapper;

    public UsersServiceImpl(ImagesService imagesService, UserRepository userRepository, UserDetailsManager userDetailsManager, UserMapper userMapper) {
        this.imagesService = imagesService;
        this.userRepository = userRepository;
        this.userDetailsManager = userDetailsManager;
        this.userMapper = userMapper;
    }

    // Обновление пароля
    @Override
    public void setPassword(NewPassword newPassword) throws ForbiddenException {
        userDetailsManager.changePassword(newPassword.getCurrentPassword(), newPassword.getNewPassword());
    }

    // Получение информации об авторизованном пользователе
    @Override
    public User getUser() {
        UserModel userModel = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userMapper.makeUser(userModel);
    }

    // Обновление информации об авторизованном пользователе
    @Override
    public UpdateUser updateUser(UpdateUser user) {
        UserModel userModel = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        userModel.setFirstName(user.getFirstName());
        userModel.setLastName(user.getLastName());
        userModel.setPhone(user.getPhone());
        userRepository.save(userModel);

        return user;
    }

    // Обновление аватара авторизованного пользователя
    @Override
    public void updateUserImage(MultipartFile image) throws IOException {
        UserModel userModel = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String targetFileName= imagesService.makeTargetFileName(image.getOriginalFilename(), userModel.getId(), FileTypeEnum.AVATAR);
        imagesService.saveFileToDisk(image, targetFileName);
        userModel.setImage(targetFileName);
        userRepository.save(userModel);
    }
}
