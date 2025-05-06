package ru.skypro.homework.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserMapperTest {
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private User author;
    private UserDTO userDTO;
    private UpdateUserDTO updateUserDTO;

    @BeforeEach
    void setUp() {
        author = new User();
        author.setId(99);
        author.setPhone("91123455");
        author.setLastName("Ivanov");
        author.setFirstName("John");

        Image authorImage = new Image();
        authorImage.setFilePath("avatar.jpg");
        author.setImage(authorImage);

        userDTO = new UserDTO();
        userDTO.setFirstName("John");
        userDTO.setPhone("91123455");

        updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFirstName("John");
        updateUserDTO.setPhone("91123455");
    }

    @Test
    void testToDtoUserDTO() {
        UserDTO mappedDto = userMapper.toDtoUserDTO(author);
        assertNotNull(mappedDto);
        assertEquals(author.getFirstName(), mappedDto.getFirstName());
        assertEquals(author.getPhone(), mappedDto.getPhone());
    }

    @Test
    void testToDtoUpdateUserDTO() {
        UpdateUserDTO mappedDto = userMapper.toDtoUpdateUserDTO(author);
        assertNotNull(mappedDto);
        assertEquals(author.getFirstName(), mappedDto.getFirstName());
        assertEquals(author.getPhone(), mappedDto.getPhone());
    }

    @Test
    void testToModel() {
        User mappedUser = userMapper.toModel(userDTO);
        assertNotNull(mappedUser);
        assertEquals(userDTO.getFirstName(), mappedUser.getFirstName());
        assertEquals(userDTO.getPhone(), mappedUser.getPhone());
    }
}