package ru.skypro.homework.service.map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.User;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class UserMapTest {
    @Spy
    private UserMap userMap;

    @Test
    public void registerToUserTest() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setFirstName("Test");
        user.setLastName("Testov");
        user.setPhone("+7923-923-92-39");
        user.setRole(Role.USER);

        Register register = new Register();
        register.setUsername("username");
        register.setPassword("password");
        register.setFirstName("Test");
        register.setLastName("Testov");
        register.setPhone("+7923-923-92-39");
        register.setRole(Role.USER);

        assertThat(userMap.registerToUser(register)).isEqualTo(user);
    }
}
