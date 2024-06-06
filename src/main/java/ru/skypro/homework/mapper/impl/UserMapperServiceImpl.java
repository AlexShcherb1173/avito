package ru.skypro.homework.mapper.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.ImageEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapperService;

@Service
public class UserMapperServiceImpl implements UserMapperService {

    @Value(value = "${ads.image}")
    private String linkImage;

    @Override
    public UserDto mappingToDto(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setId(userEntity.getId());
        userDto.setEmail(userEntity.getEmail());
        userDto.setFirstName(userEntity.getFirstName());
        userDto.setLastName(userEntity.getLastName());
        userDto.setPhone(userEntity.getPhone());
        userDto.setRole(userEntity.getRole());
        ImageEntity imageEntity = userEntity.getImageEntity();
        if (imageEntity != null) {
            userDto.setImage(String.format(linkImage, imageEntity.getId()));
        }
        return userDto;
    }

    @Override
    public UserEntity mappingToEntity(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userDto.getId());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setPhone(userDto.getPhone());
        return userEntity;
    }
}
