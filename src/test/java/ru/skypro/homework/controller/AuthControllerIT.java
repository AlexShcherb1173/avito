package ru.skypro.homework.controller;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.skypro.homework.ConstantGeneratorFotTest;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.service.AuthService;
import static ru.skypro.homework.ConstantGeneratorFotTest.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testLogin_Success() throws Exception {

        RegisterDto registerDto = ConstantGeneratorFotTest.registerDtoGenerator();
        authService.register(registerDto);
        // Зарегистрировали пользователя и сохранили его в базу данных

        JSONObject loginDto = new JSONObject();
        loginDto.put("username", NEW_USER_EMAIL);
        loginDto.put("password", NEW_USER_PASSWORD);
        // Подготовили объект для аутентификации пользователя

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
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testLogin_UnauthorizedException_IncorrectLogin() throws Exception {

        RegisterDto registerDto = ConstantGeneratorFotTest.registerDtoGenerator();
        authService.register(registerDto);

        JSONObject loginDto = new JSONObject();
        loginDto.put("username", USER_EMAIL);
        loginDto.put("password", NEW_USER_PASSWORD);
        // Введен некорректный логин

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .content(loginDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        // Пользовательское исключение NotFoundException, которое выбрасывается при загрузке данных пользователя из базы
        // данных в объект UserSecurityDetails, мы обернули в UnauthorizedException
    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testLogin_UnauthorizedException_IncorrectPassword() throws Exception {

        RegisterDto registerDto = ConstantGeneratorFotTest.registerDtoGenerator();
        authService.register(registerDto);

        JSONObject loginDto = new JSONObject();
        loginDto.put("username", NEW_USER_EMAIL);
        loginDto.put("password", USER_PASSWORD);
        // Введен некорректный пароль

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .content(loginDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

}
