package ru.skypro.homework.mapper;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.PhotoEntity;
import ru.skypro.homework.entity.UserEntity;

import java.io.IOException;

@Service
public class UserMapper {

    public static UserEntity mapperFromRegisterToUserEntity(Register dtoRegister) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(dtoRegister.getUsername());
        userEntity.setPassword(dtoRegister.getPassword());
        userEntity.setFirstName(dtoRegister.getFirstName());
        userEntity.setLastName(dtoRegister.getLastName());
        userEntity.setPhone(dtoRegister.getPhone());
        userEntity.setRole(dtoRegister.getRole());
        return userEntity;
    }

    public static User mapperFromUserEntityToUser(UserEntity userEntity) {
        User dtoUser = new User();
        dtoUser.setId(userEntity.getId());
        dtoUser.setEmail(userEntity.getUserName());
        dtoUser.setFirstName(userEntity.getFirstName());
        dtoUser.setLastName(userEntity.getLastName());
        dtoUser.setPhone(userEntity.getPhone());
        dtoUser.setRole(userEntity.getRole());
        dtoUser.setImage(userEntity.getImage());
        return dtoUser;
    }


    public static UpdateUser mapperFromUserEntityToUpdateUser(UserEntity userEntity) {
        UpdateUser dtoUpdateUser = new UpdateUser();
        dtoUpdateUser.setFirstName(userEntity.getFirstName());
        dtoUpdateUser.setLastName(userEntity.getLastName());
        dtoUpdateUser.setPhone(userEntity.getPhone());
        return dtoUpdateUser;
    }

    public static UserEntity mapFromRegisterToUserEntity(Register dto) {
        UserEntity entity = new UserEntity();
        entity.setUserName(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPhone(dto.getPhone());
        entity.setRole(dto.getRole());
        return entity;

    }

    public PhotoEntity mapMultipartFileToPhoto(MultipartFile image) {
        PhotoEntity photo = new PhotoEntity();
        try {
            photo.setData(image.getBytes());
            photo.setMediaType(image.getContentType());
            photo.setFileSize(image.getSize());
        } catch (IOException e) {
            throw new RuntimeException("Ошибка конвертации MultipartFile в PhotoEntity, " +
                    "место ошибки - userMapper.mapMultiPartFileToPhoto()");
        }
        return photo;
    }
}