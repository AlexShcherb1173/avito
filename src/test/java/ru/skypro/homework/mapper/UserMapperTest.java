package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.entity.UserEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void toEntity_FromRegister_ShouldMapCorrectly() {
        Register register = new Register();
        register.setUsername("test@example.com");
        register.setPassword("password");
        register.setFirstName("John");
        register.setLastName("Doe");
        register.setPhone("+79999999999");
        register.setRole(Role.USER);

        UserEntity entity = userMapper.toEntity(register);

        assertNull(entity.getId());
        assertEquals("test@example.com", entity.getEmail());
        assertEquals("John", entity.getFirstName());
        assertEquals("Doe", entity.getLastName());
        assertEquals("+79999999999", entity.getPhone());
        assertEquals(Role.USER, entity.getRole());
        assertNull(entity.getImage());
        assertNull(entity.getPassword()); // Password должен устанавливаться отдельно
    }

    @Test
    void toDto_FromEntity_ShouldMapCorrectly() {
        UserEntity entity = new UserEntity();
        entity.setId(1);
        entity.setEmail("test@example.com");
        entity.setFirstName("John");
        entity.setLastName("Doe");
        entity.setPhone("+79999999999");
        entity.setRole(Role.USER);
        entity.setImage("avatar.jpg");
        entity.setPassword("encodedPassword");

        ru.skypro.homework.dto.User dto = userMapper.toDto(entity);

        assertEquals(1, dto.getId());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("+79999999999", dto.getPhone());
        assertEquals(Role.USER, dto.getRole());
        assertEquals("avatar.jpg", dto.getImage());
    }

    @Test
    void updateEntityFromDto_ShouldUpdateCorrectFields() {
        UserEntity entity = new UserEntity();
        entity.setId(1);
        entity.setEmail("old@example.com");
        entity.setFirstName("OldFirstName");
        entity.setLastName("OldLastName");
        entity.setPhone("+78888888888");
        entity.setRole(Role.USER);
        entity.setImage("old.jpg");
        entity.setPassword("oldPassword");

        UpdateUser updateUser = new UpdateUser();
        updateUser.setFirstName("NewFirstName");
        updateUser.setLastName("NewLastName");
        updateUser.setPhone("+79999999999");

        userMapper.updateEntityFromDto(updateUser, entity);

        // Проверяем, что обновились только разрешенные поля
        assertEquals("NewFirstName", entity.getFirstName());
        assertEquals("NewLastName", entity.getLastName());
        assertEquals("+79999999999", entity.getPhone());

        // Проверяем, что остальные поля не изменились
        assertEquals(1, entity.getId());
        assertEquals("old@example.com", entity.getEmail());
        assertEquals(Role.USER, entity.getRole());
        assertEquals("old.jpg", entity.getImage());
        assertEquals("oldPassword", entity.getPassword());
    }
}