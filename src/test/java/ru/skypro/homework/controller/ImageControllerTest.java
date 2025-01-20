package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.repository.ImageRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ImageControllerTest {

    public static final String AUTH_NAME = "Username from authentification";
    public static final int IMAGE_ID = 111;
    public static final String IMAGE_FILEPATH = "src/test/resources/jpg.png";
    public static final String IMAGE_CONTENT_TYPE = MediaType.IMAGE_PNG_VALUE;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ImageRepository imageRepository;
    @Autowired
    private WebApplicationContext context;

    @Test
    @WithMockUser(username = AUTH_NAME)
    void downloadImageTest() throws Exception {

        byte[] bytes = Files.readAllBytes(Path.of(IMAGE_FILEPATH));

        Image image = new Image();
        image.setFilePath(IMAGE_FILEPATH);
        image.setMediaType(IMAGE_CONTENT_TYPE);

        when(imageRepository.findById(eq(IMAGE_ID))).thenReturn(Optional.of(image));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/image/{id}", IMAGE_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(IMAGE_CONTENT_TYPE))
                .andExpect(content().bytes(bytes));

        when(imageRepository.findById(eq(IMAGE_ID))).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/image/{id}", IMAGE_ID))
                .andExpect(status().isNotFound());
    }
}