package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.ImageEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapperService;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.utils.WorkWithFilesUtils;
import java.io.*;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Value(value = "${photo.dir.path}")
    private String pathDir;

    private final UserRepository userRepository;
    private final UserMapperService userMapperService;
    private final PasswordEncoder passwordEncoder;
    private final ImageRepository imageRepository;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapperService userMapperService,
                           PasswordEncoder passwordEncoder, ImageRepository imageRepository) {
        this.userRepository = userRepository;
        this.userMapperService = userMapperService;
        this.passwordEncoder = passwordEncoder;
        this.imageRepository = imageRepository;
    }

    @Override
    public boolean setPassword(NewPasswordDto newPasswordDto) {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(getUserName());
        if (userEntityOptional.isEmpty()) {
            return false;
        }
        UserEntity userEntity = userEntityOptional.get();
        if (!passwordEncoder.matches(newPasswordDto.getCurrentPassword(), userEntity.getPassword())) {
            return false;
        }
        userEntity.setPassword(passwordEncoder.encode(newPasswordDto.getNewPassword()));
        userRepository.save(userEntity);
        return true;
    }

    @Override
    public UserDto getUser() {
        String userName = getUserName();
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(userName);
        if (userEntityOptional.isEmpty()) {
            return null;
        }
        UserEntity userEntity = userEntityOptional.get();
        return userMapperService.mappingToDto(userEntity);
    }

    @Override
    public UpdateUserDto updateUser(UpdateUserDto updateUserDto) {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(getUserName());
        if (userEntityOptional.isEmpty()) {
            return null;
        }
        UserEntity userEntity = userEntityOptional.get();
        userEntity.setFirstName(updateUserDto.getFirstName());
        userEntity.setLastName(updateUserDto.getLastName());
        userEntity.setPhone(updateUserDto.getPhone());
        userRepository.save(userEntity);
        return updateUserDto;
    }

    @Override
    public void updateUserImage(MultipartFile file) throws IOException {
        String userName = getUserName();
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(userName);
        if (userEntityOptional.isEmpty()) {
            return;
        }
        UserEntity userEntity = userEntityOptional.get();
        ImageEntity curImage = userEntity.getImageEntity();
        ImageEntity imageEntity = WorkWithFilesUtils.loadImage(file, pathDir);
        if (curImage != null) {
            String pathFile = pathDir + "/" + curImage.getNameImage();
            WorkWithFilesUtils.deleteFile(pathFile);
            imageEntity.setId(curImage.getId());
        }
        imageRepository.save(imageEntity);
        userEntity.setImageEntity(imageEntity);
        userRepository.save(userEntity);
    }

    @Override
    public byte[] getImage() {
        String userName = getUserName();
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(userName);
        if (userEntityOptional.isEmpty()) {
            return null;
        }
        ImageEntity imageEntity = userEntityOptional.get().getImageEntity();
        if (imageEntity == null) {
            return null;
        }
        return WorkWithFilesUtils.getImage(imageEntity.getNameImage(), pathDir);
    }

    private String getUserName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}
