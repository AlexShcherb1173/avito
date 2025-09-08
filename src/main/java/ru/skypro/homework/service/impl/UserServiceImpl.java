package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.security.UserPrincipal;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthServiceImpl authService;

    private static final String UPLOAD_DIR = "uploads/users/";

    @Override
    public User getCurrentUser(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userMapper.toDto(userPrincipal.getUserEntity());
    }

    @Override
    @Transactional
    public User updateUser(UpdateUser updateUser, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity userEntity = userPrincipal.getUserEntity();

        userMapper.updateEntityFromDto(updateUser, userEntity);
        userRepository.save(userEntity);

        return userMapper.toDto(userEntity);
    }

    @Override
    @Transactional
    public void updateUserImage(MultipartFile image, Authentication authentication) throws IOException {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity userEntity = userPrincipal.getUserEntity();

        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);

        // Save file
        Files.copy(image.getInputStream(), filePath);

        // Update user entity
        userEntity.setImagePath(filename);
        userRepository.save(userEntity);
    }

    @Override
    public UserEntity getCurrentUserEntity(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.getUserEntity();
    }

    @Override
    public byte[] getUserImage(String filename) throws IOException {
        Path path = Paths.get(UPLOAD_DIR, filename);
        return Files.readAllBytes(path);
    }

    @Override
    public boolean changePassword(String currentPassword, String newPassword, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity userEntity = userPrincipal.getUserEntity();

        // Verify current password
        if (!authService.login(userEntity.getEmail(), currentPassword)) {
            return false;
        }

        // Update password
        userEntity.setPassword(newPassword);
        userRepository.save(userEntity);
        return true;
    }
}