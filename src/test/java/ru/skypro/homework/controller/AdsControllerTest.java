package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdsControllerTest {

    public static final int PK = 111;
    public static final int SECOND_PK = 222;
    public static final int THIRD_PK = 333;
    public static final String AUTH_NAME = "AuthName";
    public static final String TITLE = "Title";
    public static final String DESCRIPTION = "Description";
    public static final int PRICE = 100;
    public static final Integer USER_ID = 123;
    public static final Integer WRONG_USER_ID = 1234;
    public static final Integer ADMIN_ID = 321;
    public static final String USER_FIRSTNAME = "User firstname";
    public static final String USER_LASTNAME = "User lastname";
    public static final String USER_EMAIL = "User email";
    public static final String USER_PHONE = "User phone";
    public static final String USER_NAME = "User";
    public static final String WRONG_USER_NAME = "Wrong User";
    public static final String ADMIN_NAME = "Admin";
    public static final String TEST_IMAGE_PATH = "src/test/resources/jpg.png";
    public static final String TEST_IMAGE_FILENAME = "jpg.png";
    public static final String SECOND_TITLE = "Second title";
    public static final String THIRD_TITLE = "Third title";
    public static final int IMAGE_ID = 1111;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AdsRepository adsRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private ImageRepository imageRepository;
    @Autowired
    private WebApplicationContext context;

    @Test
    void getAdsAllTest() throws Exception {

        User user = new User();
        user.setId(USER_ID);

        Image image = new Image();
        image.setId(IMAGE_ID);

        Ads firstAd = new Ads();
        firstAd.setPk(PK);
        firstAd.setAuthor(user);
        firstAd.setImage(image);

        Ads secondAd = new Ads();
        secondAd.setPk(SECOND_PK);
        secondAd.setAuthor(user);
        secondAd.setImage(image);

        Ads thirdAd = new Ads();
        thirdAd.setPk(THIRD_PK);
        thirdAd.setAuthor(user);
        thirdAd.setImage(image);

        when(adsRepository.findAll()).thenReturn(List.of(firstAd, secondAd, thirdAd));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(3))
                .andExpect(jsonPath("$.results", hasSize(3)))
                .andExpect(jsonPath("$.results[0].pk").value(PK))
                .andExpect(jsonPath("$.results[1].pk").value(SECOND_PK))
                .andExpect(jsonPath("$.results[2].pk").value(THIRD_PK));
    }

    @Test
    @WithMockUser(username = AUTH_NAME)
    void addAdTest() throws Exception {

        User user = new User();
        user.setId(USER_ID);

        CreateAdsDto createAdsDto = new CreateAdsDto();
        createAdsDto.setTitle(TITLE);
        createAdsDto.setDescription(DESCRIPTION);
        createAdsDto.setPrice(PRICE);

        byte[] imageFile = Files.readAllBytes(Path.of(TEST_IMAGE_PATH));
        MockPart imagePart = new MockPart("image", TEST_IMAGE_FILENAME, imageFile);
        imagePart.getHeaders().setContentType(MediaType.IMAGE_PNG);

        byte[] properties = objectMapper.writeValueAsBytes(createAdsDto);
        MockPart propertiesPart = new MockPart("properties", properties);
        propertiesPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        when(userRepository.findByUserName(eq(AUTH_NAME))).thenReturn(Optional.of(user));
        when(adsRepository.save(any(Ads.class)))
                .thenAnswer(input -> {
                    Ads savedAd = input.getArgument(0);
                    savedAd.setPk(PK);
                    return savedAd;
                });
        when(imageRepository.save(any(Image.class)))
                .thenAnswer(input -> {
                    Image savedImage = input.getArgument(0);
                    savedImage.setId(IMAGE_ID);
                    return savedImage;
                });

        mockMvc.perform(multipart(HttpMethod.POST, "/ads")
                        .part(imagePart)
                        .part(propertiesPart)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.author").value(USER_ID))
                .andExpect(jsonPath("$.title").value(TITLE))
                .andExpect(jsonPath("$.image").value("/image/" + IMAGE_ID))
                .andExpect(jsonPath("$.price").value(PRICE))
                .andExpect(jsonPath("$.pk").value(PK));
    }

    @Test
    @WithMockUser(username = AUTH_NAME)
    void getAdTest() throws Exception {

        User user = new User();
        user.setId(USER_ID);
        user.setFirstName(USER_FIRSTNAME);
        user.setLastName(USER_LASTNAME);
        user.setEmail(USER_EMAIL);
        user.setPhone(USER_PHONE);

        Image image = new Image();
        image.setId(IMAGE_ID);

        Ads ad = new Ads();
        ad.setPk(PK);
        ad.setTitle(TITLE);
        ad.setDescription(DESCRIPTION);
        ad.setPrice(PRICE);
        ad.setAuthor(user);
        ad.setImage(image);

        when(adsRepository.findById(eq(PK))).thenReturn(Optional.of(ad));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads/{id}", PK))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(PK))
                .andExpect(jsonPath("$.authorFirstName").value(USER_FIRSTNAME))
                .andExpect(jsonPath("$.authorLastName").value(USER_LASTNAME))
                .andExpect(jsonPath("$.description").value(DESCRIPTION))
                .andExpect(jsonPath("$.email").value(USER_EMAIL))
                .andExpect(jsonPath("$.image").value("/image/" + IMAGE_ID))
                .andExpect(jsonPath("$.phone").value(USER_PHONE))
                .andExpect(jsonPath("$.price").value(PRICE))
                .andExpect(jsonPath("$.title").value(TITLE));
    }

    @Test
    @WithMockUser(username = AUTH_NAME)
    void deleteAdTest() throws Exception {

        User user = new User();
        user.setUserName(USER_NAME);
        user.setRole(Role.USER);

        User wrongUser = new User();
        wrongUser.setUserName(WRONG_USER_NAME);
        wrongUser.setRole(Role.USER);

        User admin = new User();
        admin.setUserName(ADMIN_NAME);
        admin.setRole(Role.ADMIN);

        Ads ad = new Ads();
        ad.setAuthor(user);
        ad.setCommentsList(List.of());

        when(userRepository.findByUserName(eq(AUTH_NAME))).thenReturn(Optional.of(user));
        when(adsRepository.findById(eq(PK))).thenReturn(Optional.of(ad));
        doNothing().when(adsRepository).delete(eq(ad));
        doNothing().when(commentRepository).deleteAll(any(List.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/{id}", PK))
                .andExpect(status().isOk());

        when(userRepository.findByUserName(eq(AUTH_NAME))).thenReturn(Optional.of(wrongUser));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/{id}", PK))
                .andExpect(status().isForbidden());

        when(userRepository.findByUserName(eq(AUTH_NAME))).thenReturn(Optional.of(admin));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/{id}", PK))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = AUTH_NAME)
    void updateAdTest() throws Exception {

        User user = new User();
        user.setId(USER_ID);
        user.setUserName(USER_NAME);
        user.setRole(Role.USER);

        User wrongUser = new User();
        wrongUser.setId(WRONG_USER_ID);
        wrongUser.setUserName(WRONG_USER_NAME);
        wrongUser.setRole(Role.USER);

        User admin = new User();
        admin.setId(ADMIN_ID);
        admin.setUserName(ADMIN_NAME);
        admin.setRole(Role.ADMIN);

        Image image = new Image();
        image.setId(IMAGE_ID);

        Ads ad = new Ads();
        ad.setPk(PK);
        ad.setAuthor(user);
        ad.setImage(image);

        Ads savedAd = new Ads();
        savedAd.setPk(PK);
        savedAd.setAuthor(user);
        savedAd.setImage(image);
        savedAd.setTitle(TITLE);
        savedAd.setDescription(DESCRIPTION);
        savedAd.setPrice(PRICE);

        JSONObject createAdsDto = new JSONObject();
        createAdsDto.put("title", TITLE);
        createAdsDto.put("description", DESCRIPTION);
        createAdsDto.put("price", PRICE);

        when(userRepository.findByUserName(eq(AUTH_NAME))).thenReturn(Optional.of(user));
        when(adsRepository.findById(eq(PK))).thenReturn(Optional.of(ad));
        when(adsRepository.save(eq(ad))).thenReturn(savedAd);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/ads/{id}", PK)
                        .content(createAdsDto.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value(USER_ID))
                .andExpect(jsonPath("$.image").value("/image/" + IMAGE_ID))
                .andExpect(jsonPath("$.pk").value(PK))
                .andExpect(jsonPath("$.price").value(PRICE))
                .andExpect(jsonPath("$.title").value(TITLE));

        when(userRepository.findByUserName(eq(AUTH_NAME))).thenReturn(Optional.of(wrongUser));
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/ads/{id}", PK)
                        .content(createAdsDto.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        when(userRepository.findByUserName(eq(AUTH_NAME))).thenReturn(Optional.of(admin));
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/ads/{id}", PK)
                        .content(createAdsDto.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value(USER_ID))
                .andExpect(jsonPath("$.image").value("/image/" + IMAGE_ID))
                .andExpect(jsonPath("$.pk").value(PK))
                .andExpect(jsonPath("$.price").value(PRICE))
                .andExpect(jsonPath("$.title").value(TITLE));
    }

    @Test
    @WithMockUser(username = AUTH_NAME)
    void getAdsMeTest() throws Exception {

        User user = new User();
        user.setId(USER_ID);
        user.setRole(Role.USER);

        Image image = new Image();
        image.setFilePath("images\\image.jpg");

        Ads firstAd = new Ads();
        firstAd.setPk(PK);
        firstAd.setAuthor(user);
        firstAd.setImage(image);

        Ads secondAd = new Ads();
        secondAd.setPk(SECOND_PK);
        secondAd.setAuthor(user);
        secondAd.setImage(image);

        Ads thirdAd = new Ads();
        thirdAd.setPk(THIRD_PK);
        thirdAd.setAuthor(user);
        thirdAd.setImage(image);

        when(adsRepository.findByAuthor_UserName(eq(AUTH_NAME))).thenReturn(List.of(firstAd, secondAd, thirdAd));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(3))
                .andExpect(jsonPath("$.results", hasSize(3)))
                .andExpect(jsonPath("$.results[0].pk").value(PK))
                .andExpect(jsonPath("$.results[1].pk").value(SECOND_PK))
                .andExpect(jsonPath("$.results[2].pk").value(THIRD_PK));
    }

    @Test
    @WithMockUser(username = AUTH_NAME)
    void updateImageTest() throws Exception {

        User user = new User();

        Ads ad = new Ads();
        ad.setPk(PK);

        byte[] testImageBytes = Files.readAllBytes(Path.of(TEST_IMAGE_PATH));
        MockPart testImagePart = new MockPart("image", TEST_IMAGE_FILENAME, testImageBytes);
        testImagePart.getHeaders().setContentType(MediaType.IMAGE_PNG);

        Image image = new Image();
        image.setFilePath("\\images\\" + TEST_IMAGE_FILENAME);
        image.setFileSize(testImageBytes.length);
        image.setMediaType(MediaType.IMAGE_PNG_VALUE);

        when(userRepository.findByUserName(eq(AUTH_NAME))).thenReturn(Optional.of(user));
        when(adsRepository.findById(eq(PK))).thenReturn(Optional.of(ad));
        when(adsRepository.save(any(Ads.class))).thenReturn(ad);
        when(imageRepository.save(eq(image))).thenReturn(image);

        mockMvc.perform(
                        multipart(HttpMethod.PATCH, "/ads/{id}/image", PK)
                                .part(testImagePart))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes(testImageBytes));
    }

    @Test
    @WithMockUser(username = AUTH_NAME)
    void getAdsByNamePart() throws Exception {

        User user = new User();
        user.setId(USER_ID);

        Image image = new Image();
        image.setId(IMAGE_ID);

        Ads firstAd = new Ads();
        firstAd.setPk(PK);
        firstAd.setAuthor(user);
        firstAd.setImage(image);
        firstAd.setTitle(TITLE);

        Ads secondAd = new Ads();
        secondAd.setPk(SECOND_PK);
        secondAd.setAuthor(user);
        secondAd.setImage(image);
        secondAd.setTitle(SECOND_TITLE);

        Ads thirdAd = new Ads();
        thirdAd.setPk(THIRD_PK);
        thirdAd.setAuthor(user);
        thirdAd.setImage(image);
        thirdAd.setTitle(THIRD_TITLE);

        when(adsRepository.findByTitleContainingIgnoreCase(eq("title"))).thenReturn(List.of(firstAd, secondAd, thirdAd));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads/title/{titlePart}", "title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(3))
                .andExpect(jsonPath("$.results", hasSize(3)))
                .andExpect(jsonPath("$.results[0].pk").value(PK))
                .andExpect(jsonPath("$.results[1].pk").value(SECOND_PK))
                .andExpect(jsonPath("$.results[2].pk").value(THIRD_PK));
    }

    @Test
    void unauthenticatedTests() throws Exception {

        mockMvc.perform(multipart(HttpMethod.POST, "/ads")).andExpect(status().isUnauthorized());
        mockMvc.perform(MockMvcRequestBuilders.get("/ads/{id}", PK)).andExpect(status().isUnauthorized());
        mockMvc.perform(MockMvcRequestBuilders.delete("/ads/{id}", PK)).andExpect(status().isUnauthorized());
        mockMvc.perform(MockMvcRequestBuilders.patch("/ads/{id}", PK)).andExpect(status().isUnauthorized());
        mockMvc.perform(MockMvcRequestBuilders.get("/ads/me")).andExpect(status().isUnauthorized());
        mockMvc.perform(multipart(HttpMethod.PATCH, "/ads/{id}/image", PK)).andExpect(status().isUnauthorized());
        mockMvc.perform(MockMvcRequestBuilders.get("/ads/title/{titlePart}", "title"))
                .andExpect(status().isUnauthorized());
    }
}