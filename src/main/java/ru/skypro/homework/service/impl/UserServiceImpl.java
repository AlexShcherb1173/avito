package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private ImageService imageService;

    @Override
    public UserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = repository.findByEmail(authentication.getName());
        return UserMapper.INSTANCE.toUserDTO(user);
    }

    @Override
    public UserDto updateUser(UpdateUserDto updateUserDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = repository.findByEmail(authentication.getName());
        UserMapper.INSTANCE.updateUserDTOToUser(updateUserDto, user);
        return UserMapper.INSTANCE.toUserDTO(repository.save(user));
    }

    @Override
    public Void setPassword(NewPasswordDto newPasswordDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = repository.findByEmail(authentication.getName());

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!passwordEncoder.matches(newPasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }

        String updatePassword = passwordEncoder.encode(newPasswordDto.getNewPassword());
        user.setPassword(updatePassword);
        repository.save(user);
        return null;
    }

    @Override
    public void updateUserImage(MultipartFile image, String email) {
        User user = repository.findByEmail(email);
        if (user.getImage() == null) {
            user.setImage(imageService.createImage(image));
            repository.save(user);
            return;
        }
        Long imageId = user.getImage().getId();
        user.setImage(imageService.createImage(image));
        imageService.deleteImage(imageId);
        repository.save(user);
    }
}

