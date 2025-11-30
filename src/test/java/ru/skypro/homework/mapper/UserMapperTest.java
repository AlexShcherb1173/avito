package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.dto.Role;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toEntity_ShouldMapRegisterToUserEntity() {
        // Given
        Register register = new Register();
        register.setUsername("test@example.com");
        register.setPassword("password");
        register.setFirstName("John");
        register.setLastName("Doe");
        register.setPhone("+79999999999");
        register.setRole(Role.USER);

        // When
        UserEntity userEntity = userMapper.toEntity(register);

        // Then
        assertNotNull(userEntity);
        assertEquals("test@example.com", userEntity.getEmail());
        assertEquals("John", userEntity.getFirstName());
        assertEquals("Doe", userEntity.getLastName());
        assertEquals("+79999999999", userEntity.getPhone());
        assertEquals(Role.USER, userEntity.getRole());
    }

    @Test
    void toDto_ShouldMapUserEntityToUser() {
        // Given
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setEmail("test@example.com");
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setPhone("+79999999999");
        userEntity.setRole(Role.USER);
        userEntity.setImage("avatar.jpg");

        // When
        User user = userMapper.toDto(userEntity);

        // Then
        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("+79999999999", user.getPhone());
        assertEquals(Role.USER, user.getRole());
        assertEquals("/images/avatar.jpg", user.getImage());
    }

    @Test
    void toDto_ShouldHandleNullImage() {
        // Given
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setEmail("test@example.com");
        userEntity.setImage(null);

        // When
        User user = userMapper.toDto(userEntity);

        // Then
        assertNotNull(user);
        assertNull(user.getImage());
    }

    @Test
    void updateEntityFromDto_ShouldUpdateUserEntity() {
        // Given
        UpdateUser updateUser = new UpdateUser();
        updateUser.setFirstName("Jane");
        updateUser.setLastName("Smith");
        updateUser.setPhone("+78888888888");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setEmail("old@example.com");
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setPhone("+79999999999");
        userEntity.setRole(Role.USER);
        userEntity.setPassword("password");

        // When
        userMapper.updateEntityFromDto(updateUser, userEntity);

        // Then
        assertEquals("Jane", userEntity.getFirstName());
        assertEquals("Smith", userEntity.getLastName());
        assertEquals("+78888888888", userEntity.getPhone());
        // Ensure other fields are not changed
        assertEquals(1, userEntity.getId());
        assertEquals("old@example.com", userEntity.getEmail());
        assertEquals(Role.USER, userEntity.getRole());
        assertEquals("password", userEntity.getPassword());
    }
}