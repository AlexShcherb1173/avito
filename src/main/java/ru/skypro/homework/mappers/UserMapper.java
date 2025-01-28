package ru.skypro.homework.mappers;

import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.UserEntity;

public class UserMapper {

    public UserEntity createLogin(Login login) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(login.getUsername());
        userEntity.setPassword(login.getPassword());
        return userEntity;

    }

    public UserEntity updatePassword(NewPassword newPassword, UserEntity userEntity) {
        userEntity.setPassword(newPassword.getNewPassword());
        return userEntity;
    }
    public UserEntity registerUser(Register user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userEntity.getUserName());
        userEntity.setPassword(userEntity.getPassword());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setPhone(user.getPhone());
        userEntity.setRole(userEntity.getRole());
        return userEntity;
    }
    public UserEntity updateUser(UpdateUser user, UserEntity userEntity) {
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setPhone(user.getPhone());
        return userEntity;

    }

    public UserEntity createUser(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(user.getEmail());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setPhone(user.getPhone());
        userEntity.setRole(userEntity.getRole());
        userEntity.setProfilePicture(user.getImage());
        return userEntity;
    }

}
