package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.entity.Picture;
import ru.skypro.homework.entity.Role;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.User;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    Picture picture = Picture.builder().id(1).build();

    @Test
    void testUserToUserDto() {
        // Given
        User user = User.builder().id(1).firstName("John").lastName("Doe").email("john@example.com").phone("+123456789").image(picture).build();

        // When
        UserDto userDto = userMapper.UserToUserDto(user);

        // Then
        assertNotNull(userDto);
        assertEquals("John", userDto.getFirstName());
        assertEquals("Doe", userDto.getLastName());
        assertEquals("john@example.com", userDto.getEmail());
        assertEquals("+123456789", userDto.getPhone());
        assertEquals("/pictures/1", userDto.getImage());
    }

    @Test
    void testUserToUpdateUserDto() {
        // Given
        User user = User.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .phone("+71234567890")
                .email("john@example.com")
                .role(Role.USER)
                .image(picture)
                .build();

        // When
        UpdateUserDto updateUserDto = userMapper.UserToUpdateUserDto(user);

        // Then
        assertNotNull(updateUserDto);
        assertEquals("John", updateUserDto.getFirstName());
        assertEquals("Doe", updateUserDto.getLastName());
        assertEquals("+71234567890", updateUserDto.getPhone());
    }

    @Test
    void testSafeUpdateUserDtoToUserEntity() {
        // Given
        User existingUser = User.builder().id(1).firstName("OldFirstName").lastName("OldLastName").phone("+111111111").email("old@example.com").image(picture).build();

        UpdateUserDto updateDto = new UpdateUserDto();
        updateDto.setFirstName("NewFirstName");
        updateDto.setLastName("NewLastName");
        updateDto.setPhone("+999999999");

        // When
        userMapper.safeUpdateUserDtoToUserEntity(updateDto, existingUser);

        // Then
        assertEquals("NewFirstName", existingUser.getFirstName());
        assertEquals("NewLastName", existingUser.getLastName());
        assertEquals("+999999999", existingUser.getPhone());
        assertEquals(1, existingUser.getId());
        assertEquals("old@example.com", existingUser.getEmail());
        assertEquals(picture, existingUser.getImage());
    }

    @Test
    void testSafeUpdateUserDtoToUserEntity_PartialUpdate() {
        // Given
        User existingUser = User.builder().id(1).firstName("OldFirstName").lastName("OldLastName").phone("+111111111").build();

        UpdateUserDto updateDto = new UpdateUserDto();
        updateDto.setFirstName("NewFirstName");

        // When
        userMapper.safeUpdateUserDtoToUserEntity(updateDto, existingUser);

        // Then
        assertEquals("NewFirstName", existingUser.getFirstName());
        assertEquals("OldLastName", existingUser.getLastName());
        assertEquals("+111111111", existingUser.getPhone());
    }

    @Test
    void testSafeUpdateUserDtoToUserEntity_WithNullAndEmpty() {
        // Given
        User existingUser = User.builder().id(1).firstName("John").lastName("Doe").phone("+123456789").build();

        UpdateUserDto updateDto = new UpdateUserDto();
        updateDto.setFirstName("");
        updateDto.setLastName(null);
        updateDto.setPhone("  ");

        // When
        userMapper.safeUpdateUserDtoToUserEntity(updateDto, existingUser);

        // Then
        assertEquals("John", existingUser.getFirstName());
        assertEquals("Doe", existingUser.getLastName());
        assertEquals("+123456789", existingUser.getPhone());
    }

    @Test
    void testRegisterDtoToUserEntity() {
        // Given
        RegisterDto registerDto = RegisterDto.builder().username("newuser@example.com").password("password123").firstName("New").lastName("User").phone("+123456789").role(Role.valueOf("USER")).build();

        // When
        User user = userMapper.RegisterDtoToUserEntity(registerDto);

        // Then
        assertNotNull(user);
        assertEquals("newuser@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("New", user.getFirstName());
        assertEquals("User", user.getLastName());
        assertEquals("+123456789", user.getPhone());
        assertEquals(Role.valueOf("USER"), user.getRole());
        assertNull(user.getId());
        assertNull(user.getAds());
        assertNull(user.getComments());
        assertNull(user.getImage());
    }

    @Test
    void testNullSafety() {
        assertDoesNotThrow(() -> {
            userMapper.UserToUserDto(null);
            userMapper.RegisterDtoToUserEntity(null);

            User user = new User();
            userMapper.safeUpdateUserDtoToUserEntity(null, user);
        });
    }
}