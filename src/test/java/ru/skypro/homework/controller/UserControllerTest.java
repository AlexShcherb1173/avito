package ru.skypro.homework.controller;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    public static final String AUTH_NAME = "Username from authentification";
    public static final String USERNAME = "Username";
    public static final String PASSWORD = "Password";
    public static final String NEW_PASSWORD = "New password";
    public static final String WRONG_PASSWORD = "Wrong password";
    public static final int USER_ID = 111;
    public static final String EMAIL = "user@gmail.com";
    public static final String FIRSTNAME = "Firstname";
    public static final String LASTNAME = "Lastname";
    public static final String PHONE = "+7(777)7777777";
    public static final String IMAGE_FILEPATH = "src/test/resources/jpg.png";
    public static final String IMAGE_FILENAME = "jpg.png";
    public static final int IMAGE_ID = 999;
    public static final String NEW_FIRSTNAME = "New firstname";
    public static final String NEW_LASTNAME = "New lastname";
    public static final String NEW_PHONE = "+8(888)8888888";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder encoder;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ImageRepository imageRepository;
    @Autowired
    private WebApplicationContext context;

    @Test
    @WithMockUser(username = AUTH_NAME)
    void setPasswordTest() throws Exception {

        User user = new User();
        user.setUserName(USERNAME);
        user.setPassword(encoder.encode(PASSWORD));

        User wrongUser = new User();
        wrongUser.setUserName(USERNAME);
        wrongUser.setPassword(encoder.encode(WRONG_PASSWORD));

        JSONObject newPassword = new JSONObject();
        newPassword.put("currentPassword", PASSWORD);
        newPassword.put("newPassword", NEW_PASSWORD);

        when(userRepository.findByUserName(eq(AUTH_NAME))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(input -> input.getArgument(0));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPassword.toString()))
                .andExpect(status().isOk());

        when(userRepository.findByUserName(eq(AUTH_NAME))).thenReturn(Optional.of(wrongUser));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPassword.toString()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = AUTH_NAME)
    void getUserTest() throws Exception {

        Image image = new Image();
        image.setId(IMAGE_ID);

        User user = new User();
        user.setId(USER_ID);
        user.setEmail(EMAIL);
        user.setFirstName(FIRSTNAME);
        user.setLastName(LASTNAME);
        user.setPhone(PHONE);
        user.setImage(image);

        when(userRepository.findByUserName(eq(AUTH_NAME))).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.firstName").value(FIRSTNAME))
                .andExpect(jsonPath("$.lastName").value(LASTNAME))
                .andExpect(jsonPath("$.phone").value(PHONE))
                .andExpect(jsonPath("$.image").value("/image/" + IMAGE_ID));
    }

    @Test
    @WithMockUser(username = AUTH_NAME)
    void updateUserTest() throws Exception {

        Image image = new Image();
        image.setId(IMAGE_ID);

        User user = new User();
        user.setId(USER_ID);
        user.setEmail(EMAIL);
        user.setFirstName(FIRSTNAME);
        user.setLastName(LASTNAME);
        user.setPhone(PHONE);
        user.setImage(image);

        JSONObject userJson = new JSONObject();
        userJson.put("id", 0);
        userJson.put("email", null);
        userJson.put("firstName", NEW_FIRSTNAME);
        userJson.put("lastName", NEW_LASTNAME);
        userJson.put("phone", NEW_PHONE);
        userJson.put("image", null);

        when(userRepository.findByUserName(eq(AUTH_NAME))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(input -> input.getArgument(0));

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.firstName").value(NEW_FIRSTNAME))
                .andExpect(jsonPath("$.lastName").value(NEW_LASTNAME))
                .andExpect(jsonPath("$.phone").value(NEW_PHONE))
                .andExpect(jsonPath("$.image").value("/image/" + IMAGE_ID));
    }

    @Test
    @WithMockUser(username = AUTH_NAME)
    void updateUserImageTest() throws Exception {

        User user = new User();

        byte[] testImageBytes = Files.readAllBytes(Path.of(IMAGE_FILEPATH));
        MockPart testImagePart = new MockPart("image", IMAGE_FILENAME, testImageBytes);
        testImagePart.getHeaders().setContentType(MediaType.IMAGE_PNG);

        Image image = new Image();
        image.setFilePath("\\images\\" + IMAGE_FILENAME);
        image.setFileSize(testImageBytes.length);
        image.setMediaType(MediaType.IMAGE_PNG_VALUE);

        when(userRepository.findByUserName(eq(AUTH_NAME))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(input -> input.getArgument(0));
        when(imageRepository.save(any(Image.class))).thenAnswer(input -> input.getArgument(0));

        mockMvc.perform(multipart(HttpMethod.PATCH, "/users/me/image")
                        .part(testImagePart))
                .andExpect(status().isOk());
    }

    @Test
    void unauthenticatedTests() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/users/set_password")).andExpect(status().isUnauthorized());
        mockMvc.perform(MockMvcRequestBuilders.get("/users/me")).andExpect(status().isUnauthorized());
        mockMvc.perform(MockMvcRequestBuilders.patch("/users/me")).andExpect(status().isUnauthorized());
        mockMvc.perform(multipart(HttpMethod.PATCH, "/users/me/image")).andExpect(status().isUnauthorized());
    }
}