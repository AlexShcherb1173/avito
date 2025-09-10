package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.ConstantGeneratorFotTest;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.ImageUploadService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.service.impl.AdServiceImpl;
import ru.skypro.homework.service.impl.SecurityServiceImpl;
import ru.skypro.homework.service.impl.UserSecurityDetails;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static ru.skypro.homework.ConstantGeneratorFotTest.*;

@WebMvcTest(controllers = AdController.class)
public class AdControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdRepository adRepository;

    @MockBean
    private AdMapper adMapper;

    @MockBean
    private ImageUploadService imageUploadService;

    @MockBean
    private UserService userService;

    @MockBean
    private SecurityServiceImpl securityService;

    @MockBean
    private UserSecurityDetails userSecurityDetails;

    @SpyBean
    private AdServiceImpl adService;

    @InjectMocks
    private AdController adController;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private User newUser;
    private User userAdmin;
    private Ad ad1;
    private Ad newAd1;
    private Ad newAd2;
    private AdDto adDto1;
    private AdDto newAdDto1;
    private AdDto newAdDto2;
    private ExtendedAdDto extendedAdDto;
    private Ad ad2;
    private List<Ad> ads;
    private List<AdDto> adsDto;
    private CreateOrUpdateAdDto createOrUpdateAdDto1;
    private CreateOrUpdateAdDto createOrUpdateAdDto2;


    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();
        user = ConstantGeneratorFotTest.userGenerator();
        newUser = ConstantGeneratorFotTest.newUserGenerator_1();
        userAdmin = ConstantGeneratorFotTest.userAdminGenerator();
        ad1 = ConstantGeneratorFotTest.adGenerator1();
        // Первое объявление, созданное пользователем user
        newAd1 = ConstantGeneratorFotTest.newAdGenerator1();
        // Первое объявление с обновленными заголовком и ценой
        newAd2 = ConstantGeneratorFotTest.newAdGenerator2();
        // Первое объявление с обновленным путем к файлу
        adDto1 = ConstantGeneratorFotTest.adDtoGenerator1();
        // Модель для первого объявления
        ad2 = ConstantGeneratorFotTest.adGenerator2();
        // Второе объявление
        ads = ConstantGeneratorFotTest.listAdsGenerator();
        // Список объявлений
        adsDto = ConstantGeneratorFotTest.listAdsDtoGenerator();
        // Модель списка объявлений
        createOrUpdateAdDto1 = ConstantGeneratorFotTest.createOrUpdateAdDtoGenerator1();
        // Модель для создания первого объявления
        createOrUpdateAdDto2 = ConstantGeneratorFotTest.createOrUpdateAdDtoGenerator2();
        // Модель для обновления первого объявления
        extendedAdDto = ConstantGeneratorFotTest.extendedAdDtoGenerator();
        // Полное первое объявление
        newAdDto1 = ConstantGeneratorFotTest.newAdDtoGenerator();
        // Модель с обновленными заголовком и ценой для первого объявления
        newAdDto2 = ConstantGeneratorFotTest.newAdDtoGenerator2();
        // Модель с обновленным путем к файлу для первого объявления
    }


    // Нужно разобраться, почему к объявлениям отсутствует доступ у не аутентифицированного пользователя
    @WithMockUser(username = USER_EMAIL)
    @Test
    public void testGetAds_Success() throws Exception {

        when(adRepository.findAll()).thenReturn(ads);
        when(adMapper.toDtos(anyList())).thenReturn(adsDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results.size()").value(adsDto.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].author")
                        .value(adsDto.get(0).getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].image")
                        .value(adsDto.get(0).getImage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].pk").value(adsDto.get(0).getPk()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].title")
                        .value(adsDto.get(0).getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].price")
                        .value(adsDto.get(0).getPrice()));
    }


    @WithMockUser(username = USER_EMAIL)
    @Test
    public void testGetAdsByAuthenticatedUser_Success() throws Exception {

        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        // Когда вызываем метод getAuthenticatedUserName(), то ожидаем получить из объекта Authentication логин
        // текущего аутентифицированного пользователя. Аутентифицированный объект Authentication сохраняется в SecurityContext.
        // Но если объект Authentication еще не заполнен полностью (в частности, в нем отсутствуют права пользователя),
        // то это означает, что пользователь еще не прошел аутентификацию, и объект Authentication не сохранен
        // в SecurityContext.
        when(userService.getUserByEmailFromDb(anyString())).thenReturn(user);
        when(adRepository.findAllByUserId(user.getId())).thenReturn(ads);
        when(adMapper.toDtos(anyList())).thenReturn(adsDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results.size()").value(adsDto.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].author")
                        .value(adsDto.get(0).getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].image")
                        .value(adsDto.get(0).getImage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].pk").value(adsDto.get(0).getPk()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].title")
                        .value(adsDto.get(0).getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].price")
                        .value(adsDto.get(0).getPrice()));
    }


    //@WithAnonymousUser
    @Test
    public void testGetAdsByAuthenticatedUser_Unauthorized() throws Exception {

        when(userSecurityDetails.getUsername()).thenReturn (null);
        // Если UserSecurityDetails содержит null, значит объект Authentication полностью не заполнен и не сохранен
        // в SecurityContext, то есть пользователь не аутентифицирован.
        // Мы имитируем эту ситуацию и проверяем, что контроллер возвращает правильный HTTP статус

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }


    @WithMockUser(username = USER_EMAIL)
    @Test
    public void testGetExtendedAd_Success() throws Exception {

        when(adRepository.findById(anyLong())).thenReturn(Optional.of(ad1));
        when(adMapper.toExtendedDto(any(Ad.class))).thenReturn(extendedAdDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads/{id}", ad1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pk").value(extendedAdDto.getPk()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(extendedAdDto.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(extendedAdDto.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description")
                        .value(extendedAdDto.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.image").value(extendedAdDto.getImage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(extendedAdDto.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorFirstName")
                        .value(extendedAdDto.getAuthorFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorLastName")
                        .value(extendedAdDto.getAuthorLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value(extendedAdDto.getPhone()));
    }

    //@WithAnonymousUser
    @Test
    public void testGetExtendedAd_Unauthorized() throws Exception {

        when(userSecurityDetails.getUsername()).thenReturn (null);
        // Если UserSecurityDetails содержит null, значит объект Authentication полностью не заполнен и не сохранен
        // в SecurityContext, то есть пользователь не аутентифицирован.
        // Мы имитируем эту ситуацию и проверяем, что контроллер возвращает правильный HTTP статус

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads/{id}", ad1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @WithMockUser(username = USER_EMAIL)
    @Test
    public void testGetExtendedAd_NotFound() throws Exception {

        doReturn(Optional.empty()).when(adRepository).findById(1L);
        doThrow(new NotFoundException("Объявление не найдено")).when(adService).getExtendedAd(1L);
        // Имитируем ситуацию, при которой объявление не найдено, и метод выбрасывает исключение.
        // Исключение выбрасывает зависимый, а не тестируемый класс

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @WithMockUser(username = USER_EMAIL)
    @Test
    public void testAddAd_Success() throws Exception {

        // В этом случае будет загружен пустой файл
        MultipartFile image = mock(MultipartFile.class);
        MockMultipartFile propertiesJson = new MockMultipartFile(
                 "properties",
                  "test.jpg",
                 "application/json",
                 objectMapper.writeValueAsBytes(createOrUpdateAdDto1));

        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userService.getUserByEmailFromDb(anyString())).thenReturn(user);
        doNothing().when(adMapper).updateAdFromUpdateAdDto(any(CreateOrUpdateAdDto.class), any(Ad.class));
        when(adRepository.save(any(Ad.class))).thenReturn(ad1);
        when(adMapper.toDto(any(Ad.class))).thenReturn(adDto1);

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/ads")
                        .file("image", image.getBytes())
                        .file(propertiesJson)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(adDto1.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pk").value(adDto1.getPk()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(adDto1.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(adDto1.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.image").value(adDto1.getImage()));

        // CSRF защита: Использование метода .with(csrf()) в запросе позволяет mock деталям передать CSRF-токен.
        // Если в вашем приложении включена защита от Cross-Site Request Forgery (CSRF), Spring Security требует,
        // чтобы все изменяющие запросы (например, POST, PUT, DELETE) содержали действительный CSRF-токен.
        // Иначе запросы будут отклонены с ошибкой 403 Forbidden. Добавив .with(csrf()), вы создаете валидный токен для теста.
    }


    @Test
    public void testAddAd_UnauthorizedException() throws Exception {

        // В этом случае будет загружен пустой файл
        MultipartFile image = mock(MultipartFile.class);
        MockMultipartFile propertiesJson = new MockMultipartFile(
                "properties",
                "properties.json",
                "application/json",
                "{\"title\": \"Title 1\", \"price\": 5000, \"description\": \"Description 1\"}".getBytes());

        when(userSecurityDetails.getUsername()).thenReturn (null);
        // Если UserSecurityDetails содержит null, значит объект Authentication полностью не заполнен и не сохранен
        // в SecurityContext, то есть пользователь не аутентифицирован.
        // Мы имитируем эту ситуацию и проверяем, что контроллер возвращает правильный HTTP статус

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/ads")
                        .file("image", image.getBytes())
                        .file(propertiesJson)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }


    @WithMockUser
    @Test
    public void testAddAd_InvalidData() throws Exception {

        // В этом случае будет загружен пустой файл
        MultipartFile image = mock(MultipartFile.class);
        MockMultipartFile propertiesJson = new MockMultipartFile(
                "properties",
                "properties.json",
                "application/json",
                "{\"title\": \"Abc\", \"price\": 12000000, \"description\": \"Description 1\"}".getBytes());

        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userService.getUserByEmailFromDb(anyString())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/ads")
                        .file("image", image.getBytes())
                        .file(propertiesJson)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @WithMockUser(username = USER_EMAIL)
    @Test
    public void testUpdateAd_userIsAuthorOfAd_Success() throws Exception {

        JSONObject createOrUpdateAdDto = new JSONObject();
        createOrUpdateAdDto.put("title", AD_NEW_TITLE_1);
        createOrUpdateAdDto.put("price", AD_NEW_PRICE_1);
        createOrUpdateAdDto.put("description", AD_NEW_DESCRIPTION_1);

        // Пользователь прошел аутентификацию с логина USER_EMAIL, у которого роль User.
        // Данный пользователь является автором объявления.
        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userService.getUserByEmailFromDb(anyString())).thenReturn(user);

        when(adRepository.findById(anyLong())).thenReturn(Optional.of(ad1));
        when(adService.isAdCreatorOrAdmin(anyLong())).thenReturn(true);
        doNothing().when(adMapper).updateAdFromUpdateAdDto(any(CreateOrUpdateAdDto.class), any(Ad.class));
        when(adRepository.save(any(Ad.class))).thenReturn(newAd1);
        when(adMapper.toDto(any(Ad.class))).thenReturn(newAdDto1);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/ads/{id}", ad1.getId())
                        .content(createOrUpdateAdDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(AD_NEW_TITLE_1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(AD_NEW_PRICE_1));
        // Поле "description" из jsonPath извлечь нельзя, так как метод возвращает AdDto (а в нем нет такого поля)
    }


    @WithMockUser(username = USER_ADMIN_EMAIL)
    @Test
    public void testUpdateAd_Admin_Success() throws Exception {

        JSONObject createOrUpdateAdDto = new JSONObject();
        createOrUpdateAdDto.put("title", AD_NEW_TITLE_1);
        createOrUpdateAdDto.put("price", AD_NEW_PRICE_1);
        createOrUpdateAdDto.put("description", AD_NEW_DESCRIPTION_1);

        // Пользователь прошел аутентификацию с логина USER_ADMIN_EMAIL, у которого роль Admin.
        // Данный пользователь не является автором объявления. Но у админа есть права на обновление объявления
        when(securityService.getAuthenticatedUserName()).thenReturn(userAdmin.getEmail());
        when(userService.getUserByEmailFromDb(anyString())).thenReturn(userAdmin);

        when(adRepository.findById(anyLong())).thenReturn(Optional.of(ad1));
        when(adService.isAdCreatorOrAdmin(anyLong())).thenReturn(true);
        doNothing().when(adMapper).updateAdFromUpdateAdDto(any(CreateOrUpdateAdDto.class), any(Ad.class));
        when(adRepository.save(any(Ad.class))).thenReturn(newAd1);
        when(adMapper.toDto(any(Ad.class))).thenReturn(newAdDto1);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/ads/{id}", ad1.getId())
                        .content(createOrUpdateAdDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(AD_NEW_TITLE_1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(AD_NEW_PRICE_1));
        // Поле "description" из jsonPath извлечь нельзя, так как метод возвращает AdDto (а в нем нет такого поля)
    }


    //@WithAnonymousUser
    @Test
    public void testUpdateAd_Unauthorized() throws Exception {

        JSONObject createOrUpdateAdDto = new JSONObject();
        createOrUpdateAdDto.put("title", AD_NEW_TITLE_1);
        createOrUpdateAdDto.put("price", AD_NEW_PRICE_1);
        createOrUpdateAdDto.put("description", AD_NEW_DESCRIPTION_1);

        when(userSecurityDetails.getUsername()).thenReturn (null);
        // Если UserSecurityDetails содержит null, значит объект Authentication полностью не заполнен и не сохранен
        // в SecurityContext, то есть пользователь не аутентифицирован.
        // Мы имитируем эту ситуацию и проверяем, что контроллер возвращает правильный HTTP статус

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/ads/{id}", ad1.getId())
                        .content(createOrUpdateAdDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }


    @WithMockUser(username = USER_EMAIL)
    @Test
    public void testUpdateAd_NotFoundException() throws Exception {

        JSONObject createOrUpdateAdDto = new JSONObject();
        createOrUpdateAdDto.put("title", AD_NEW_TITLE_1);
        createOrUpdateAdDto.put("price", AD_NEW_PRICE_1);
        createOrUpdateAdDto.put("description", AD_NEW_DESCRIPTION_1);

        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userService.getUserByEmailFromDb(user.getEmail())).thenReturn(user);

        doReturn(Optional.empty()).when(adRepository).findById(1L);
        doThrow(new NotFoundException("Объявление не найдено")).when(adService).updateAd(1L, createOrUpdateAdDto2);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/ads/1")
                        .content(createOrUpdateAdDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @WithMockUser(username = USER_EMAIL)
    @Test
    public void testUpdateAd_InvalidData() throws Exception {

        JSONObject createOrUpdateAdDto = new JSONObject();
        createOrUpdateAdDto.put("title", AD_INVALID_TITLE_1);
        createOrUpdateAdDto.put("price", AD_INVALID_PRICE_1);
        createOrUpdateAdDto.put("description", AD_INVALID_DESCRIPTION_1);

        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userService.getUserByEmailFromDb(user.getEmail())).thenReturn(user);
        when(adRepository.findById(anyLong())).thenReturn(Optional.ofNullable(ad1));

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/ads/{id}", ad1.getId())
                        .content(createOrUpdateAdDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @WithMockUser(username = NEW_USER_EMAIL)
    @Test
    public void testUpdateAd_AccessDeniedException() throws Exception {

        // Пользователь прошел аутентификацию с логина NEW_USER_EMAIL, у которого роль User.
        // Данный пользователь не является автором объявления.
        JSONObject createOrUpdateAdDto = new JSONObject();
        createOrUpdateAdDto.put("title", AD_NEW_TITLE_1);
        createOrUpdateAdDto.put("price", AD_NEW_PRICE_1);
        createOrUpdateAdDto.put("description", AD_NEW_DESCRIPTION_1);

        when(securityService.getAuthenticatedUserName()).thenReturn(newUser.getEmail());
        when(userService.getUserByEmailFromDb(anyString())).thenReturn(newUser);

        when(adRepository.findById(anyLong())).thenReturn(Optional.of(ad1));
        when(adService.isAdCreatorOrAdmin(anyLong())).thenReturn(false);
        doThrow(new AccessDeniedException("У пользователя отсутствуют права на изменение объявления"))
                .when(adMapper).updateAdFromUpdateAdDto(createOrUpdateAdDto2, ad1);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/ads/{id}", ad1.getId())
                        .content(createOrUpdateAdDto.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


    @WithMockUser(username = USER_EMAIL)
    @Test
    public void testUpdateImageAd_UserIsAuthorOfAd_Success() throws Exception {

        // Необходимо добавить в папку images файл с именем - AD_NEW_IMAGE_1
        Path path = Path.of("images/" + AD_NEW_IMAGE_1);
        String contentType = Files.probeContentType(path);
        byte[] content = Files.readAllBytes(path);
        MockMultipartFile image = new MockMultipartFile("image", AD_NEW_IMAGE_1, contentType, content);
        // name = "image", так как в эндпоинте указано: @RequestPart("image") MultipartFile image

        // Пользователь прошел аутентификацию с логина USER_EMAIL, у которого роль User.
        // Данный пользователь является автором объявления.
        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userService.getUserByEmailFromDb(anyString())).thenReturn(user);

        when(adRepository.findById(anyLong())).thenReturn(Optional.of(ad1));
        when(adService.isAdCreatorOrAdmin(anyLong())).thenReturn(true);
        when(adRepository.save(any(Ad.class))).thenReturn(newAd2);
        // Сохраняем в базу данных Ad c обновленным путем к файлу
        when(adMapper.toDto(any(Ad.class))).thenReturn(newAdDto2);

        MockHttpServletRequestBuilder requestBuilder =
                multipart("/ads/{id}/image", ad1.getId())
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(csrf());
        // В отсутствие последней строки будет выдаваться ошибка 403

        requestBuilder.with(request -> {
            request.setMethod("PATCH");
            return request;
        });

        // Выполнение запроса и проверка результата
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.image").value(newAdDto2.getImage()));
    }


    @WithMockUser(username = USER_ADMIN_EMAIL)
    @Test
    public void testUpdateImageAd_Admin_Success() throws Exception {

        // Необходимо добавить в папку images файл с именем - AD_NEW_IMAGE_1
        Path path = Path.of("images/" + AD_NEW_IMAGE_1);
        String contentType = Files.probeContentType(path);
        byte[] content = Files.readAllBytes(path);
        MockMultipartFile image = new MockMultipartFile("image", AD_NEW_IMAGE_1, contentType, content);

        // Пользователь прошел аутентификацию с логина USER_ADMIN_EMAIL, у которого роль Admin.
        // Данный пользователь не является автором объявления, но обладает правами на изменение картинки объявления.
        when(securityService.getAuthenticatedUserName()).thenReturn(userAdmin.getEmail());
        when(userService.getUserByEmailFromDb(anyString())).thenReturn(userAdmin);

        when(adRepository.findById(anyLong())).thenReturn(Optional.of(ad1));
        when(adService.isAdCreatorOrAdmin(anyLong())).thenReturn(true);
        when(adRepository.save(any(Ad.class))).thenReturn(newAd2);
        // Сохраняем в базу данных Ad c обновленным путем к файлу
        when(adMapper.toDto(any(Ad.class))).thenReturn(newAdDto2);

        MockHttpServletRequestBuilder requestBuilder =
                multipart("/ads/{id}/image", ad1.getId())
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(csrf());

        requestBuilder.with(request -> {
            request.setMethod("PATCH");
            return request;
        });

        // Выполнение запроса и проверка результата
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.image").value(newAdDto2.getImage()));
    }


    @WithMockUser(username = USER_EMAIL)
    @Test
    public void testRemoveAd_UserIsAuthorOfAd_Success() throws Exception {

        // Пользователь прошел аутентификацию с логина USER_EMAIL
        // Пользователь является автором объявления
        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userService.getUserByEmailFromDb(user.getEmail())).thenReturn(user);

        when(adRepository.findById(anyLong())).thenReturn(Optional.of(ad2));
        when(adService.isAdCreatorOrAdmin(ad2.getId())).thenReturn(true);
        doNothing().when(adRepository).deleteById(ad2.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/{id}", ad2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


    @WithMockUser(username = USER_ADMIN_EMAIL)
    @Test
    public void testRemoveAd_Admin_Success() throws Exception {

        // Пользователь прошел аутентификацию с логина USER_ADMIN_EMAIL, у которого роль админ.
        // Пользователь не является автором объявления, но обладает правами на удаление объявления.
        when(securityService.getAuthenticatedUserName()).thenReturn(userAdmin.getEmail());
        when(userService.getUserByEmailFromDb(anyString())).thenReturn(userAdmin);

        when(adRepository.findById(anyLong())).thenReturn(Optional.of(ad2));
        when(adService.isAdCreatorOrAdmin(ad2.getId())).thenReturn(true);
        doNothing().when(adRepository).deleteById(ad2.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/{id}", ad2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


    //@WithAnonymousUser
    @Test
    public void testRemoveAd_Unauthorized() throws Exception {

        when(userSecurityDetails.getUsername()).thenReturn(null);
        // Пользователь не прошел аутентификацию, поэтому объект userSecurityDetails равен null. Объект Authentication
        // полностью не заполнен (в частности, отсутствуют права пользователя) и не сохранен в SecurityContext.

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/{id}", ad2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }


    @WithMockUser(username = USER_EMAIL)
    @Test
    public void testRemoveAd_NotFound() throws Exception {

        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userService.getUserByEmailFromDb(anyString())).thenReturn(user);

        doReturn(Optional.empty()).when(adRepository).findById(1L);
        doThrow(new NotFoundException("Объявление не найдено")).when(adService).removeAd(1L);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @WithMockUser(username = NEW_USER_EMAIL)
    @Test
    public void testRemoveAd_ForbiddenException_1() throws Exception {

        // Пользователь прошел аутентификацию с логина NEW_USER_EMAIL, у которого роль User.
        // Поэтому текущий аутентифицированный пользователь не является автором объявления.
        when(securityService.getAuthenticatedUserName()).thenReturn(newUser.getEmail());
        when(userService.getUserByEmailFromDb(anyString())).thenReturn(newUser);

        when(adRepository.findById(anyLong())).thenReturn(Optional.of(ad2));
        when(adService.isAdCreatorOrAdmin(ad2.getId())).thenReturn(false);
        doThrow(new AccessDeniedException("У пользователя отсутствуют права на удаление объявления"))
                .when(adRepository).deleteById(ad2.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/{id}", ad2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


    @WithMockUser(username = USER_EMAIL)
    @Test
    public void testRemoveAd_ForbiddenException_2() throws Exception {

        // Пользователь прошел аутентификацию с логина USER_EMAIL, у которого роль User
        when(securityService.getAuthenticatedUserName()).thenReturn(user.getEmail());
        when(userService.getUserByEmailFromDb(anyString())).thenReturn(user);
        // Изменяем пользователя
        user.setId(1L);
        user.setEmail(NEW_USER_EMAIL);
        // Изменяем у объявления поле - пользователь
        ad2.setAuthor(user);
        // Теперь текущий аутентифицированный пользователь не является автором объявления
        when(adRepository.findById(anyLong())).thenReturn(Optional.of(ad2));
        when(adService.isAdCreatorOrAdmin(ad2.getId())).thenReturn(false);
        doThrow(new AccessDeniedException("У пользователя отсутствуют права на удаление объявления"))
                .when(adRepository).deleteById(ad2.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/{id}", ad2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


}
