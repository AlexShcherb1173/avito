package ru.skypro.homework.controller;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.skypro.homework.ConstantGeneratorFotTest;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

import java.nio.file.Files;
import java.nio.file.Path;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.skypro.homework.ConstantGeneratorFotTest.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;


    @BeforeEach
    void setUp() {

        RegisterDto registerDto = ConstantGeneratorFotTest.registerDtoGenerator();
        authService.register(registerDto);
        // Зарегистрировали пользователя и сохранили его в базу данных.
        // Пользователь имеет логин NEW_USER_EMAIL и пароль NEW_USER_PASSWORD.
        // В данном случае используем метод по регистрации пользователя, а не метод по сохранению пользователя в базе
        // данных по той причине, что необходимо закодировать пароль пользователя и сохранить его в базу данных.
    }


    @Test
    @WithMockUser(username = NEW_USER_EMAIL)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdatePassword_Success() throws Exception {

        JSONObject newPasswordDto = new JSONObject();
        newPasswordDto.put("currentPassword", NEW_USER_PASSWORD);
        newPasswordDto.put("newPassword", NEW_USER_PASSWORD_2);

        // Изменяем пароль
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/set_password")
                        .content(newPasswordDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Осуществляем аутентификацию пользователя при введении нового пароля
        JSONObject loginDto = new JSONObject();
        loginDto.put("username", NEW_USER_EMAIL);
        loginDto.put("password", NEW_USER_PASSWORD_2);

        String contentAsString = mockMvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .content(loginDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertEquals("true", contentAsString);
    }

    @Test
    @WithMockUser(username = NEW_USER_EMAIL)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdatePassword_InvalidData() throws Exception {

        JSONObject newPasswordDto = new JSONObject();
        newPasswordDto.put("currentPassword", NEW_USER_PASSWORD);
        newPasswordDto.put("newPassword", USER_INVALID_PASSWORD);
        // Введенный пароль содержит 5 символов

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/set_password")
                        .content(newPasswordDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = NEW_USER_EMAIL)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdatePassword_UnauthorizedException() throws Exception {

        JSONObject newPasswordDto = new JSONObject();
        newPasswordDto.put("currentPassword", USER_INCORRECT_PASSWORD);
        // Введен некорректный текущий пароль
        newPasswordDto.put("newPassword", NEW_USER_PASSWORD_2);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/set_password")
                        .content(newPasswordDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = USER_EMAIL)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdatePassword_ForbiddenException() throws Exception {

        // Пользователь с логина USER_EMAIL пытается изменить пароль другого пользователя с логина NEW_USER_EMAIL

        JSONObject newPasswordDto = new JSONObject();
        newPasswordDto.put("currentPassword", NEW_USER_PASSWORD);
        // Введен некорректный текущий пароль
        newPasswordDto.put("newPassword", NEW_USER_PASSWORD_2);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/set_password")
                        .content(newPasswordDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


    @WithMockUser(username = NEW_USER_EMAIL)
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testGetAuthenticatedUser_Success() throws Exception {

        // Создаем ожидаемую модель пользователя
        UserDto expected = UserDto.builder()
                .id(1L)
                .email(NEW_USER_EMAIL)
                .firstName(NEW_USER_FIRST_NAME)
                .lastName(NEW_USER_FIRST_NAME)
                .phone(NEW_USER_PHONE)
                .role(USER_ROLE)
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expected.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(expected.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(expected.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(expected.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value(expected.getPhone()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value(expected.getRole().name()));
    }

    @WithAnonymousUser
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testGetAuthenticatedUser_UnauthorizedException() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }


    @WithMockUser(username = NEW_USER_EMAIL)
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdateAuthenticatedUserInfo_Success() throws Exception {

        // Готовим Json для обновления данных пользователя
        JSONObject updateUserDto = new JSONObject();
        updateUserDto.put("firstName", NEW_USER_FIRST_NAME_2);
        updateUserDto.put("lastName", NEW_USER_LAST_NAME_2);
        updateUserDto.put("phone", NEW_USER_PHONE_2);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/users/me")
                        .content(updateUserDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                // id остался прежним
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(NEW_USER_EMAIL))
                // Логин остался прежним
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(NEW_USER_FIRST_NAME_2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(NEW_USER_LAST_NAME_2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value(NEW_USER_PHONE_2));
    }

    @WithAnonymousUser
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdateAuthenticatedUserInfo_UnauthorizedException() throws Exception {

        JSONObject updateUserDto = new JSONObject();
        updateUserDto.put("firstName", NEW_USER_FIRST_NAME_2);
        updateUserDto.put("lastName", NEW_USER_LAST_NAME_2);
        updateUserDto.put("phone", NEW_USER_PHONE_2);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/users/me")
                        .content(updateUserDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }


    @WithMockUser(username = NEW_USER_EMAIL)
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdateAuthenticatedUserImage_Success() throws Exception {

        // В папку "images" необходимо поместить аватарку пользователя с именем файла 3.jpg
        // На текущий момент у зарегистрированного пользователя с логином NEW_USER_EMAIL отсутствует аватарка

        Path path = Path.of("images/" + NEW_USER_IMAGE);
        String contentType = Files.probeContentType(path);
        byte[] content = Files.readAllBytes(path);
        MockMultipartFile image = new MockMultipartFile("image", NEW_USER_IMAGE, contentType, content);
        // name = "image", так как в эндпоинте указано: @RequestPart("image") MultipartFile image

        MockHttpServletRequestBuilder requestBuilder =
                multipart("/users/me/image")
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE);

        requestBuilder.with(request -> {
            request.setMethod("PATCH");
            return request;
        });

        // Выполнение запроса и проверка результата
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @WithAnonymousUser
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testUpdateAuthenticatedUserImage_UnauthorizedException() throws Exception {

        Path path = Path.of("images/" + NEW_USER_IMAGE);
        String contentType = Files.probeContentType(path);
        byte[] content = Files.readAllBytes(path);
        MockMultipartFile image = new MockMultipartFile("image", NEW_USER_IMAGE, contentType, content);
        // name = "image", так как в эндпоинте указано: @RequestPart("image") MultipartFile image

        MockHttpServletRequestBuilder requestBuilder =
                multipart("/users/me/image")
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE);

        requestBuilder.with(request -> {
            request.setMethod("PATCH");
            return request;
        });

        // Выполнение запроса и проверка результата
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testDownloadImageForUser_Success() throws Exception {

        // Перед вызовом этого теста в базе данных создается пользователь с логином NEW_USER_EMAIL, но без аватарки
        User user = userService.getUserByEmailFromDb(NEW_USER_EMAIL);
        // Находим этого пользователя в базе данных по логину
        user.setImage(NEW_USER_IMAGE);
        // Сохраняем в поле "image" этого пользователя имя файла
        userRepository.save(user);
        // Снова сохраняем пользователя в базу данных

        // Тестируем метод по выгрузке аватарки из файловой системы в профиль пользователя
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/{imagePath}", NEW_USER_IMAGE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.IMAGE_JPEG_VALUE));
                 // IMAGE_JPEG_VALUE или IMAGE_JPEG
    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testDownloadImageForUser_NotFoundException() throws Exception {

        // Перед вызовом этого теста в базе данных создается пользователь с логином NEW_USER_EMAIL, но без аватарки
        User user = userService.getUserByEmailFromDb(NEW_USER_EMAIL);
        // Находим этого пользователя в базе данных по логину
        user.setImage(NEW_USER_IMAGE);
        // Сохраняем в поле "image" этого пользователя имя файла
        userRepository.save(user);
        // Снова сохраняем пользователя в базу данных

        // Тестируем метод по выгрузке аватарки из файловой системы в профиль пользователя
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/99.jpg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
