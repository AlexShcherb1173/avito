package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    public void userToUserDtoTest() {
        //Описан User
        User user = testUser();

        //Маппинг в UserDto
        UserDto userDto = userMapper.userToUserDto(user);

        //Проверка соответствия
        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isEqualTo(user.getId());
        assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
        assertThat(userDto.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(userDto.getLastName()).isEqualTo(user.getLastName());
        assertThat(userDto.getPhone()).isEqualTo(user.getPhone());
        assertThat(userDto.getRole()).isEqualTo(user.getRole());
        assertThat(userDto.getImage()).isEqualTo(user.getAvatar().getUrl());
    }

    @Test
    public void userDtoToUserTest() {
        //Описан UserDto
        UserDto userDto = testUserDto();

        //Маппинг в User
        User user = userMapper.userDtoToUser(userDto);

        //Проверка соответствия
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(userDto.getId());
        assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(user.getFirstName()).isEqualTo(userDto.getFirstName());
        assertThat(user.getLastName()).isEqualTo(userDto.getLastName());
        assertThat(user.getPhone()).isEqualTo(userDto.getPhone());
        assertThat(user.getRole()).isEqualTo(userDto.getRole());
    }

    @Test
    public void userToUpdateUserDtoTest() {
        //Описан User
        User user = testUser();

        //Маппинг в UpdateUserDto
        UpdateUserDto updateUserDto = userMapper.userToUpdateUserDto(user);

        //Проверка соответствия
        assertThat(updateUserDto).isNotNull();
        assertThat(updateUserDto.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(updateUserDto.getLastName()).isEqualTo(user.getLastName());
        assertThat(updateUserDto.getPhone()).isEqualTo(user.getPhone());

    }

    @Test
    public void updateUserDtoToUserTest() {
        //Описан UpdateUserDto
        UpdateUserDto updateUserDto = testUpdateUserDto();

        //Маппинг в User
        User user = userMapper.updateUserDtoToUser(updateUserDto);

        //Проверка соответствия
        assertThat(user).isNotNull();
        assertThat(user.getFirstName()).isEqualTo(updateUserDto.getFirstName());
        assertThat(user.getLastName()).isEqualTo(updateUserDto.getLastName());
        assertThat(user.getPhone()).isEqualTo(updateUserDto.getPhone());

    }

    private User testUser() {
        User user = new User();
        user.setId(1);
        user.setEmail("email");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPassword("password");
        user.setPhone("+78881112233");
        user.setRole(Role.USER);

        Image avatar = new Image();
        avatar.setId(1);
        avatar.setFilePath("/image/" + user.getId());
        avatar.setFileExtension("fileExtension");
        avatar.setFileSize(10);
        avatar.setMediaType("mediaType");

        user.setAvatar(avatar);

        return user;
    }

    private UserDto testUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setEmail("email");
        userDto.setFirstName("firstName");
        userDto.setLastName("lastName");
        userDto.setPhone("+78881112233");
        userDto.setRole(Role.USER);

        return userDto;
    }

    private UpdateUserDto testUpdateUserDto() {
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setFirstName("test name");
        updateUserDto.setLastName("test last name");
        updateUserDto.setPhone("+79990008877");

        return updateUserDto;
    }

}
