package ru.avito.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.avito.dto.user.UserDto;
import ru.avito.entity.Role;
import ru.avito.entity.User;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;
    private User user;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();

        user = User.builder()
                .id(1)
                .email("user@example.com")
                .password("encoded-password")
                .firstName("Ivan")
                .lastName("Ivanov")
                .phone("+79990000001")
                .role(Role.USER)
                .image("/images/users/1/avatar.jpg")
                .build();
    }

    @Test
    void toDtoShouldMapAllFieldsCorrectly() {
        UserDto dto = userMapper.toDto(user);

        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("user@example.com", dto.getEmail());
        assertEquals("Ivan", dto.getFirstName());
        assertEquals("Ivanov", dto.getLastName());
        assertEquals("+79990000001", dto.getPhone());
        assertEquals("USER", dto.getRole());
        assertEquals("/images/users/1/avatar.jpg", dto.getImage());
    }

    @Test
    void toDtoShouldMapAdminRoleCorrectly() {
        user.setRole(Role.ADMIN);

        UserDto dto = userMapper.toDto(user);

        assertNotNull(dto);
        assertEquals("ADMIN", dto.getRole());
    }

    @Test
    void toDtoShouldKeepNullImageWhenImageIsNull() {
        user.setImage(null);

        UserDto dto = userMapper.toDto(user);

        assertNotNull(dto);
        assertNull(dto.getImage());
    }
}