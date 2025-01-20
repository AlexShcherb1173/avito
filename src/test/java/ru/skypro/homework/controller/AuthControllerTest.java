package ru.skypro.homework.controller;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    public static final String USER_NAME = "Username";
    public static final String WRONG_USER_NAME = "Wrong username";
    public static final String PASSWORD = "Password";
    public static final String WRONG_PASSWORD = "Wrong password";
    public static final String FIRSTNAME = "Firstname";
    public static final String LASTNAME = "Lastname";
    public static final String PHONE = "+5(555)5555555";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private WebApplicationContext context;

    @Test
    void loginTest() throws Exception {

        User user = new User();
        user.setUserName(USER_NAME);
        user.setPassword(encoder.encode(PASSWORD));

        JSONObject correctLoginReq = new JSONObject();
        correctLoginReq.put("username", USER_NAME);
        correctLoginReq.put("password", PASSWORD);

        JSONObject firstWrongLoginReq = new JSONObject();
        firstWrongLoginReq.put("username", WRONG_USER_NAME);
        firstWrongLoginReq.put("password", PASSWORD);

        JSONObject secondWrongLoginReq = new JSONObject();
        secondWrongLoginReq.put("username", USER_NAME);
        secondWrongLoginReq.put("password", WRONG_PASSWORD);

        when(userRepository.findByUserName(eq(USER_NAME))).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(correctLoginReq.toString()))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(firstWrongLoginReq.toString()))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(secondWrongLoginReq.toString()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void registerTest() throws Exception {

        JSONObject registerReq = new JSONObject();
        registerReq.put("username", USER_NAME);
        registerReq.put("password", PASSWORD);
        registerReq.put("firstName", FIRSTNAME);
        registerReq.put("lastName", LASTNAME);
        registerReq.put("phone", PHONE);

        User user = new User();
        user.setUserName(USER_NAME);
        user.setPassword(PASSWORD);
        user.setFirstName(FIRSTNAME);
        user.setLastName(LASTNAME);
        user.setPhone(PHONE);
        user.setEmail(USER_NAME);

        when(userRepository.findByUserName(eq(USER_NAME))).thenReturn(Optional.empty());
        when(userRepository.save(eq(user))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerReq.toString()))
                .andExpect(status().isCreated());

        when(userRepository.findByUserName(eq(USER_NAME))).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerReq.toString()))
                .andExpect(status().isBadRequest());

    }
}