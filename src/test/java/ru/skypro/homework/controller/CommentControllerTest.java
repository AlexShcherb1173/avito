package ru.skypro.homework.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.CommentService;

import java.net.URI;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;
import static ru.skypro.homework.constant.StaticForTests.*;

@Sql(scripts = {"classpath:schema.sql", "classpath:test-data.sql"}, executionPhase = BEFORE_TEST_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableTransactionManagement
class CommentControllerTest {

    @LocalServerPort
    int port;

    String baseUrl;
    @Autowired
    TestRestTemplate restTemplate;

    @MockitoSpyBean
    AdService adService;

    @MockitoSpyBean
    private CommentController commentController;

    @MockitoSpyBean
    private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void init() {
        baseUrl = "http://localhost:" + port + "/";
    }

    @Test
    @Transactional ////решает LazyInitializationException из-за lazy связи с users
    void addComment() {
        System.out.println(commentRepository.findAll());
        CommentDto comment = new CommentDto();
        comment.setAuthor(USER_USER1_ID);
        comment.setText(TEST_COMMENT_TEXT);
        ResponseEntity<CommentDto> response = restTemplate
                .withBasicAuth(USER_USER1_EMAIL, USER_USER1_PASSWORD)
                .postForEntity(baseUrl + "ads/1/comments", comment, CommentDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());//todo why not CREATED?
        assertEquals(comment.getText(), Objects.requireNonNull(response.getBody()).getText());
        Mockito.verify(commentController, Mockito.times(1)).addComment(Mockito.any(), Mockito.any());
        Mockito.verify(commentService, Mockito.times(1)).addComment(Mockito.any(), Mockito.any());

    }

    @Test
    void getComments() {
        ResponseEntity<ExtendedAdDto> response = restTemplate
                .withBasicAuth(USER_ADMIN_EMAIL, USER_ADMIN_PASSWORD)
                .getForEntity(baseUrl + "ads/1/comments", ExtendedAdDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Mockito.verify(commentController, Mockito.times(1)).getComments(1);
        Mockito.verify(commentService, Mockito.times(1)).getCommentsForAd(1);

    }

    @Test
    void updateComment() {
    }

    @Test
    void deleteComment() {
    }


    @Test
    void addAd() {
        CreateOrUpdateAdDto properties = new CreateOrUpdateAdDto();
        properties.setTitle("testTitle");
        properties.setDescription("testDescription");
        properties.setPrice(1000);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("properties", properties);
        body.add("image", new FileSystemResource(IMAGE_JPG_PATH_STRING));

        RequestEntity<MultiValueMap<String, Object>> request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create("http://localhost:" + port + "/ads"));
        ResponseEntity<AdDto> response = restTemplate.withBasicAuth(USER_ADMIN_EMAIL, USER_ADMIN_PASSWORD).exchange(request, AdDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());//todo CREATED???
    }

    @Test
    void addAdNegative() {
        CreateOrUpdateAdDto properties = new CreateOrUpdateAdDto();
        properties.setTitle("testTitle");
        properties.setDescription("testDescription");
        properties.setPrice(1000);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("properties", properties);
        body.add("image", new FileSystemResource(IMAGE_JPG_PATH_STRING));

        RequestEntity<MultiValueMap<String, Object>> request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create("http://localhost:" + port + "/ads"));
        ResponseEntity<AdDto> response = restTemplate.withBasicAuth(USER_ADMIN_EMAIL, "p@55w0rd").exchange(request, AdDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void getAllAds() {
        ResponseEntity<AdListDto> response = restTemplate.getForEntity("http://localhost:" + port + "/ads", AdListDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(9, response.getBody().getCount());
        Mockito.verify(adService, Mockito.times(1)).getAllAds();
    }

    @Test
    void getAdUnauthorized() {
        ResponseEntity<ExtendedAdDto> response = restTemplate.withBasicAuth(USER_ADMIN_EMAIL, "WRONGPassword").getForEntity(baseUrl + "ads/" + 1, ExtendedAdDto.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void getUserAds() {
        ResponseEntity<ExtendedAdDto> response = restTemplate.withBasicAuth(USER_ADMIN_EMAIL, USER_ADMIN_PASSWORD).getForEntity(baseUrl + "ads/me", ExtendedAdDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Mockito.verify(adService, Mockito.times(1)).getUserAds();
    }

    @Test
    void getAdById() {
        ResponseEntity<ExtendedAdDto> response = restTemplate.withBasicAuth(USER_ADMIN_EMAIL, USER_ADMIN_PASSWORD).getForEntity(baseUrl + "ads/" + 1, ExtendedAdDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Mockito.verify(adService, Mockito.times(1)).getAdById(1);

    }

    @Test
    void updateAd() {
    }

    @Test
    void removeAd() {
    }

    @Test
    void updateAdImage() {
    }
}