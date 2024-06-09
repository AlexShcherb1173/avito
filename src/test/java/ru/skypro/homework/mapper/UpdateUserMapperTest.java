package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.User;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UpdateUserMapperTest {

    @Autowired
    private UpdateUserMapper updateUserMapper;

    @Test
    public void testFromUser() {
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .phone("+7 (123) 456-78-99")
                .build();

        UpdateUser updateUser = updateUserMapper.fromUser(user);

        assertThat(updateUser).isNotNull();
        assertThat(updateUser.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(updateUser.getLastName()).isEqualTo(user.getLastName());
        assertThat(updateUser.getPhone()).isEqualTo(user.getPhone());
    }
}