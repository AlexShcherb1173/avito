package ru.skypro.homework.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser ;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Откат изменений в базе данных после теста
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser (username = "admin", roles = {"ADMIN"})
    public void testUpdateUser_Success() throws Exception {
        // Создаем и сохраняем тестового пользователя
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setFirstName("Old First Name");
        user.setLastName("Old Last Name");
        user.setPhone("1234567890");
        user.setUsername("admin");
        userRepository.save(user);

        // Создаем обновленный объект пользователя
        UserEntity updateUser  = new UserEntity();
        updateUser .setId(1);
        updateUser .setFirstName("New First Name");
        updateUser .setLastName("New Last Name");
        updateUser .setPhone("0987654321");

        // Выполняем PATCH запрос
        mockMvc.perform(patch("/profile/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUser )))
                .andExpect(status().isOk());

        // Проверяем, что данные были обновлены
        UserEntity updatedUser  = userRepository.findById(1).orElse(null);
        assert updatedUser  != null;
        assert "New First Name".equals(updatedUser.getFirstName());
        assert "New Last Name".equals(updatedUser.getLastName());
        assert "0987654321".equals(updatedUser.getPhone());
    }

    @Test
    public void testUpdateUser_Unauthorized() throws Exception {
        // Попытка обновления без авторизации
        UserEntity updateUser = new UserEntity();
        updateUser.setId(1);
        updateUser.setFirstName("New First Name");

        mockMvc.perform(patch("/profile/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUser )))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован"));
    }

    @Test
    @WithMockUser (username = "user", roles = {"USER"})
    public void testUpdateUser_UserNotFound() throws Exception {
        // Попытка обновления несуществующего пользователя
        User updateUser  = new User();
        updateUser.setId(999); // ID, которого нет в базе
        updateUser.setFirstName("New First Name");

        mockMvc.perform(patch("/profile/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUser )))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь не найден"));
    }

    @Test
    @WithMockUser (username = "admin", roles = {"ADMIN"})
    public void testUpdateUser_ErrorOnUpdate() throws Exception {
        // Создаем и сохраняем тестового пользователя
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setFirstName("Old First Name");
        user.setLastName("Old Last Name");
        user.setPhone("1234567890");
        user.setUsername("admin");
        userRepository.save(user);

        // Создаем объект, который вызовет ошибку при обновлении
        User updateUser  = new User();
        updateUser .setId(1);
        updateUser .setFirstName(null); // Предположим, что null вызовет ошибку

        mockMvc.perform(patch("/profile/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUser )))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ошибка при обновлении пользователя"));
    }
}