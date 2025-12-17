package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skypro.homework.config.FileStorageConfig;
import ru.skypro.homework.dto.user.Role;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.model.UserEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserMapperTest {

    private UserMapper userMapper = new UserMapper(new FileStorageConfig());

    private static final String TEST_EMAIL = "test@example.com";

    @Test
    void toDto_ShouldMapCorrectly(){
        // Given
        UserEntity entity = new UserEntity();
        entity.setId(1);
        entity.setEmail(TEST_EMAIL);
        entity.setFirstName("Ivan");
        entity.setLastName("Ivanov");
        entity.setPhone("89140001122");
        entity.setRole(Role.USER);
        entity.setImage("avatar.jpg");

        // When
        UserDto dto = userMapper.toDto(entity);

        // Then
        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals(TEST_EMAIL, dto.getEmail());
        assertEquals("Ivan", dto.getFirstName());
        assertEquals("Ivanov", dto.getLastName());
        assertEquals("89140001122", dto.getPhone());
        assertEquals("USER", dto.getRole());
        assertTrue(dto.getImage().contains("/users/image/1"));
    }

    @Test
    void toDto_WhenEntityIsNull_ShouldReturnNull(){
        // When
        UserDto dto = userMapper.toDto(null);

        // Then
        assertNull(dto);
    }

    @Test
    void toDto_WhenImageIsNull_ShouldSetImageToNull() {
        // Given
        UserEntity entity = new UserEntity();
        entity.setId(1);
        entity.setEmail(TEST_EMAIL);
        entity.setFirstName("Ivan");
        entity.setLastName("Ivanov");
        entity.setRole(Role.USER);
        entity.setImage(null); // нет аватара

        // When
        UserDto dto = userMapper.toDto(entity);

        // Then
        assertNotNull(dto);
        assertNull(dto.getImage());
    }

}
