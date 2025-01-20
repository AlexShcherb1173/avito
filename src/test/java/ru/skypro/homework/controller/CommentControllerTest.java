package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
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
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    public static final String AUTH_NAME = "Username from authentification";
    public static final int COMMENT_ID = 1;
    public static final int SECOND_COMMENT_ID = 2;
    public static final int THIRD_COMMENT_ID = 3;
    public static final int AD_ID = 111;
    public static final int USER_ID = 123;
    public static final int WRONG_USER_ID = 321;
    public static final String IMAGE_FILEPATH = "images\\jpg.png";
    public static final String WRONG_IMAGE_URL = "/img/wrong.png";
    public static final String TEXT = "Some text";
    public static final String NEW_TEXT = "Some new text";
    public static final String USER_FIRSTNAME = "Authors firstname";
    public static final String WRONG_USER_FIRSTNAME = "Authors wrong firstname";
    public static final String USERNAME = "Username";
    public static final String WRONG_USERNAME = "Wrong username";
    public static final String ADMIN_USERNAME = "Admin";
    public static final long CREATED_AT = 123456789;
    public static final long WRONG_CREATED_AT = 987654321;
    public static final int IMAGE_ID = 1111;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AdsRepository adsRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CommentRepository commentRepository;
    @Autowired
    private WebApplicationContext context;

    @Test
    @WithMockUser(username = AUTH_NAME)
    void getCommentsTest() throws Exception {

        Image image = new Image();
        image.setFilePath(IMAGE_FILEPATH);

        User author = new User();
        author.setId(USER_ID);
        author.setImage(image);

        Comment firstComment = new Comment();
        firstComment.setCommentId(COMMENT_ID);
        firstComment.setAuthor(author);

        Comment secondComment = new Comment();
        secondComment.setCommentId(SECOND_COMMENT_ID);
        secondComment.setAuthor(author);

        Comment thirdComment = new Comment();
        thirdComment.setCommentId(THIRD_COMMENT_ID);
        thirdComment.setAuthor(author);

        Ads ad = new Ads();
        ad.setPk(AD_ID);
        ad.setCommentsList(List.of(firstComment, secondComment, thirdComment));

        when(adsRepository.findById(eq(AD_ID))).thenReturn(Optional.of(ad));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads/{id}/comments", AD_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count").value(3))
                .andExpect(jsonPath("$.results", hasSize(3)))
                .andExpect(jsonPath("$.results[0].pk").value(COMMENT_ID))
                .andExpect(jsonPath("$.results[1].pk").value(SECOND_COMMENT_ID))
                .andExpect(jsonPath("$.results[2].pk").value(THIRD_COMMENT_ID));
    }

    @Test
    @WithMockUser(username = AUTH_NAME)
    void addCommentTest() throws Exception {

        Ads ad = new Ads();
        ad.setPk(AD_ID);

        Image image = new Image();
        image.setId(IMAGE_ID);

        User author = new User();
        author.setId(USER_ID);
        author.setImage(image);
        author.setFirstName(USER_FIRSTNAME);

        JSONObject createCommentDto = new JSONObject();
        createCommentDto.put("text", TEXT);

        when(adsRepository.findById(eq(AD_ID))).thenReturn(Optional.of(ad));
        when(userRepository.findByUserName(eq(AUTH_NAME))).thenReturn(Optional.of(author));
        when(commentRepository.save(any(Comment.class)))
                .thenAnswer(input -> {
                    Comment savedComment = input.getArgument(0);
                    savedComment.setCommentId(COMMENT_ID);
                    return savedComment;
                });

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/ads/{id}/comments", AD_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createCommentDto.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.author").value(USER_ID))
                .andExpect(jsonPath("$.authorImage").value("/image/" + IMAGE_ID))
                .andExpect(jsonPath("$.authorFirstName").value(USER_FIRSTNAME))
                .andExpect(jsonPath("$.pk").value(COMMENT_ID))
                .andExpect(jsonPath("$.text").value(TEXT));
    }

    @Test
    @WithMockUser(username = AUTH_NAME)
    void deleteCommentTest() throws Exception {

        User user = new User();
        user.setUserName(USERNAME);
        user.setRole(Role.USER);

        User wrongUser = new User();
        wrongUser.setUserName(WRONG_USERNAME);
        wrongUser.setRole(Role.USER);

        User admin = new User();
        admin.setUserName(ADMIN_USERNAME);
        admin.setRole(Role.ADMIN);

        Comment comment = new Comment();
        comment.setAuthor(user);

        when(userRepository.findByUserName(eq(AUTH_NAME))).thenReturn(Optional.of(user));
        when(commentRepository.findById(eq(COMMENT_ID))).thenReturn(Optional.of(comment));
        doNothing().when(commentRepository).deleteById(any(Integer.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/{adId}/comments/{commentId}", AD_ID, COMMENT_ID))
                .andExpect(status().isOk());

        when(userRepository.findByUserName(eq(AUTH_NAME))).thenReturn(Optional.of(wrongUser));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/{adId}/comments/{commentId}", AD_ID, COMMENT_ID))
                .andExpect(status().isForbidden());

        when(userRepository.findByUserName(eq(AUTH_NAME))).thenReturn(Optional.of(admin));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/{adId}/comments/{commentId}", AD_ID, COMMENT_ID))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = AUTH_NAME)
    void updateCommentTest() throws Exception {

        Image image = new Image();
        image.setId(IMAGE_ID);

        User user = new User();
        user.setUserName(USERNAME);
        user.setFirstName(USER_FIRSTNAME);
        user.setRole(Role.USER);
        user.setImage(image);
        user.setId(USER_ID);

        User wrongUser = new User();
        wrongUser.setUserName(WRONG_USERNAME);
        wrongUser.setRole(Role.USER);

        User admin = new User();
        admin.setUserName(ADMIN_USERNAME);
        admin.setRole(Role.ADMIN);

        Ads ad = new Ads();

        Comment comment = new Comment();
        comment.setCommentId(COMMENT_ID);
        comment.setAuthor(user);
        comment.setAd(ad);
        comment.setCreatedAt(CREATED_AT);
        comment.setText(TEXT);

        JSONObject commentDto = new JSONObject();
        commentDto.put("author", WRONG_USER_ID);
        commentDto.put("authorImage", WRONG_IMAGE_URL);
        commentDto.put("authorFirstName", WRONG_USER_FIRSTNAME);
        commentDto.put("createdAt", WRONG_CREATED_AT);
        commentDto.put("pk", SECOND_COMMENT_ID);
        commentDto.put("text", NEW_TEXT);

        when(userRepository.findByUserName(eq(AUTH_NAME))).thenReturn(Optional.of(user));
        when(commentRepository.findById(eq(COMMENT_ID))).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenAnswer(input -> input.getArgument(0));

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/ads/{adId}/comments/{commentId}", AD_ID, COMMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentDto.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.author").value(USER_ID))
                .andExpect(jsonPath("$.authorImage").value("/image/" + IMAGE_ID))
                .andExpect(jsonPath("$.authorFirstName").value(USER_FIRSTNAME))
                .andExpect(jsonPath("$.createdAt").value(CREATED_AT))
                .andExpect(jsonPath("$.pk").value(COMMENT_ID))
                .andExpect(jsonPath("$.text").value(NEW_TEXT));

        when(userRepository.findByUserName(eq(AUTH_NAME))).thenReturn(Optional.of(wrongUser));
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/ads/{adId}/comments/{commentId}", AD_ID, COMMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentDto.toString()))
                .andExpect(status().isForbidden());

        when(userRepository.findByUserName(eq(AUTH_NAME))).thenReturn(Optional.of(admin));
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/ads/{adId}/comments/{commentId}", AD_ID, COMMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentDto.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.author").value(USER_ID))
                .andExpect(jsonPath("$.authorImage").value("/image/" + IMAGE_ID))
                .andExpect(jsonPath("$.authorFirstName").value(USER_FIRSTNAME))
                .andExpect(jsonPath("$.createdAt").value(CREATED_AT))
                .andExpect(jsonPath("$.pk").value(COMMENT_ID))
                .andExpect(jsonPath("$.text").value(NEW_TEXT));

    }

    @Test
    void unauthenticatedTests() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/ads/{id}/comments", AD_ID))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(MockMvcRequestBuilders.post("/ads/{id}/comments", AD_ID))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(MockMvcRequestBuilders.delete("/ads/{adId}/comments/{commentId}", AD_ID, COMMENT_ID))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(MockMvcRequestBuilders.patch("/ads/{adId}/comments/{commentId}", AD_ID, COMMENT_ID))
                .andExpect(status().isUnauthorized());
    }
}