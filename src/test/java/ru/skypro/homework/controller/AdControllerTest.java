package ru.skypro.homework.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
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
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;
import static ru.skypro.homework.constant.StaticForTests.*;

@Sql(scripts = {"classpath:schema.sql", "classpath:test-data.sql"}, executionPhase = BEFORE_TEST_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EnableTransactionManagement
class AdControllerTest {

    @LocalServerPort
    int port;
    String baseUrl;

    @Autowired
    TestRestTemplate restTemplate;

    @MockitoSpyBean
    AdService adService;

    @MockitoSpyBean
    AdRepository adRepository;

    @MockitoSpyBean
    private AdController adController;

    @BeforeEach
    void init() {
        baseUrl = "http://localhost:" + port + "/";
    }

    @Test
    @Order(1)
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
        RequestEntity<MultiValueMap<String, Object>> request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(baseUrl + "ads"));
        ResponseEntity<AdDto> response = restTemplate.withBasicAuth(USER_ADMIN_EMAIL, USER_ADMIN_PASSWORD).exchange(request, AdDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());//todo Why not CREATED???
        Mockito.verify(adService, Mockito.times(1)).addAd(Mockito.any(), Mockito.any());
    }

    @Test
    void addAdNegative() {
        CreateOrUpdateAdDto properties = new CreateOrUpdateAdDto();
        properties.setTitle("testTitle");
        properties.setDescription("testDescription");
        properties.setPrice(1000);
        restTemplate.postForLocation(baseUrl + "login", new LoginDto(USER_ADMIN_PASSWORD, USER_ADMIN_EMAIL));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("properties", properties);
        body.add("image", new FileSystemResource(IMAGE_JPG_PATH_STRING));

        RequestEntity<MultiValueMap<String, Object>> request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(baseUrl + "ads"));
        ResponseEntity<AdDto> response = restTemplate.withBasicAuth(USER_ADMIN_EMAIL, "p@55w0rd").exchange(request, AdDto.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @Order(2)
    @Transactional
        ////решает LazyInitializationException из-за lazy связи с users
    void getAllAds() {
        ResponseEntity<AdListDto> response = restTemplate.getForEntity(baseUrl + "ads", AdListDto.class);
        System.out.println("================" + adRepository.findAll());
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
    @Transactional
        ////решает LazyInitializationException из-за lazy связи с users
    void updateAd() {
        int id1 = 2;
        System.out.println("================" + adRepository.findAll());
        CreateOrUpdateAdDto ad1 = AdMapper.toCreateOrUpdateAdDto(adRepository.findById(id1).orElseThrow());
        int oldPrice = ad1.getPrice();
        ad1.setPrice(oldPrice + 1);
        restTemplate.withBasicAuth(USER_ADMIN_EMAIL, USER_ADMIN_PASSWORD).patchForObject(baseUrl + "ads/" + id1, ad1, AdDto.class);
        Mockito.verify(adController, Mockito.times(1)).updateAd(Mockito.any(), Mockito.any());
        Mockito.verify(adService, Mockito.times(1)).updateAd(Mockito.any(), Mockito.any());
        assertEquals(oldPrice + 1, ad1.getPrice());

        //Попытка поменять чужое объявление
        int id2 = 5;
        CreateOrUpdateAdDto ad2 = AdMapper.toCreateOrUpdateAdDto(adRepository.findById(id2).orElseThrow());
        int oldPrice2 = ad2.getPrice();
        ad2.setPrice(oldPrice2 + 1);
        try {
            restTemplate.withBasicAuth(USER_USER1_EMAIL, USER_USER1_PASSWORD).patchForObject(baseUrl + "ads/" + id2, ad1, AdDto.class);
        } catch (Exception e) {
            System.out.println("---===---" + e.getMessage());
        }
        assertEquals(oldPrice2, adRepository.findById(id2).orElseThrow().getPrice());
    }

    @Test
    void removeAd() {
        //удалям своё объявление
        restTemplate.withBasicAuth(USER_ADMIN_EMAIL, USER_ADMIN_PASSWORD).delete(baseUrl + "ads/" + 1);
        Mockito.verify(adService, Mockito.times(1)).removeAd(1);
        //удаляем админом объявление пользователя
        restTemplate.withBasicAuth(USER_ADMIN_EMAIL, USER_ADMIN_PASSWORD).delete(baseUrl + "ads/" + 4);
        Mockito.verify(adService, Mockito.times(1)).removeAd(4);
        //удаляем чужое объявление
        restTemplate.withBasicAuth(USER_USER1_EMAIL, USER_USER1_PASSWORD).delete(baseUrl + "ads/" + 2);
        Mockito.verify(adService, Mockito.times(1)).removeAd(2);
        //удаляем уже удалённое объявление
        restTemplate.withBasicAuth(USER_USER1_EMAIL, USER_USER1_PASSWORD).delete(baseUrl + "ads/" + 1);
        assertEquals(7, adRepository.findAll().size(), "В базе данных осталось 2 объявления из 4");
    }

    @Test
    void updateAdImage() {

    }
}