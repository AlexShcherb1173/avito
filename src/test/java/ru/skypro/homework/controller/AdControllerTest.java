package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.config.WebSecurityConfig;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdExtendedDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.CheckRoleService;
import ru.skypro.homework.service.impl.AdServiceImpl;
import ru.skypro.homework.service.map.AdMap;


import java.security.Principal;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdController.class)
@Import({WebSecurityConfig.class, CheckRoleService.class})
public class AdControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AdRepository adRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CommentRepository commentRepository;
    @SpyBean
    private AdServiceImpl adService;
    @SpyBean
    private AdMap adMap;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ApplicationContext appContext;

    @Test
    @WithMockUser(username = "user")
    public void addAdTest() throws Exception {
        CreateOrUpdateAdDto createDto = new CreateOrUpdateAdDto();
        createDto.setTitle("MyAd");
        createDto.setPrice(100);
        createDto.setDescription("Description");

        User user = new User();
        user.setId(1);
        user.setUsername("user");

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("user");
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        Ad ad = adMap.toEntity(createDto, principal);
        Ad adAfterSave = adMap.toEntity(createDto, principal);
        adAfterSave.setId(11);

        when(adRepository.save(any())).thenReturn(adAfterSave);

        AdDto expectedAdDto = adMap.mapAdDto(adAfterSave);

        MockMultipartFile file = new MockMultipartFile("image", "11".getBytes());
        MockPart adPart = new MockPart("properties", objectMapper.writeValueAsString(createDto).getBytes());
        adPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(multipart(HttpMethod.POST, "/ads")
                        .file(file)
                        .part(adPart))
                .andExpect(status().isOk()).andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    AdDto actualAdDto = objectMapper.readValue(content, AdDto.class);
                    assertThat(actualAdDto).isEqualTo(expectedAdDto);
                });

        ArgumentCaptor<Ad> adCaptor = ArgumentCaptor.forClass(Ad.class);
        verify(adRepository, times(1)).save(adCaptor.capture());
        Ad adFact = adCaptor.getValue();
        assertEquals(ad.getId(), adFact.getId());
        assertEquals(ad.getUser().getId(), adFact.getUser().getId());
        assertEquals(ad.getUser().getUsername(), adFact.getUser().getUsername());
        assertEquals(ad.getPrice(), adFact.getPrice());
        assertEquals(ad.getTitle(), adFact.getTitle());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void deleteAdTest() throws Exception {
        Ad ad = new Ad();
        ad.setId(12);
        ad.setUser(new User());
        ad.getUser().setUsername("user");
        when(adRepository.findById(12)).thenReturn(Optional.of(ad));
        mockMvc.perform(delete("/ads/12")
        ).andExpect(status().isOk());
        //Проверка, что в adRepository.deleteById
        ArgumentCaptor<Integer> intCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(adRepository, times(1)).deleteById(intCaptor.capture());
        Integer fact = intCaptor.getValue();
        assertEquals(12, fact);
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    public void patchAdTest() throws Exception {
        //тело запроса с параметром
        CreateOrUpdateAdDto createAd = new CreateOrUpdateAdDto();
        createAd.setTitle("MyAd");
        createAd.setPrice(111);
        //заглушка для adRepository.findById
        Ad adForRepository = new Ad();
        adForRepository.setId(22);
        adForRepository.setTitle("OldTitle");
        when(adRepository.findById(11)).thenReturn(Optional.of(adForRepository));
        //заглушка для adRepository.save, все id разные,
        // чтобы подтвердилась передача  информации по этапам
        Ad adAfterSave = new Ad();
        adAfterSave.setId(33);
        adAfterSave.setUser(new User());
        adAfterSave.getUser().setId(32);
        adAfterSave.setTitle("NewTitle");
        //Через any(), т.к. через ad сравнение не срабатывает
        when(adRepository.save(any())).thenReturn(adAfterSave);
        //Ожидаемый результат
        AdDto expectedDto = adMap.mapAdDto(adAfterSave);

        mockMvc.perform(patch("/ads/11")
                        .content(objectMapper.writeValueAsString(createAd))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    AdDto actualDto = objectMapper.readValue(content, AdDto.class);
                    assertThat(actualDto).isEqualTo(expectedDto);
                });
        //Проверка, что в adRepository.save
        ArgumentCaptor<Ad> adCaptor = ArgumentCaptor.forClass(Ad.class);
        verify(adRepository, times(1)).save(adCaptor.capture());

        Ad adArgOfSave = adCaptor.getValue();
        //Проверка, что id на входе в save = взятому из репозитория, как цена и заголовок
        assertEquals(adForRepository.getId(), adArgOfSave.getId());
        assertEquals(createAd.getPrice(), adArgOfSave.getPrice());
        assertEquals(createAd.getTitle(), adArgOfSave.getTitle());

    }

    @Test
    @WithMockUser(username = "user")
    public void getMyAdsTest() throws Exception {
        //заглушка для userRepository.findByUsername
        User user = new User();
        user.setId(1);
        user.setUsername("user");
        //юзер содержит коллекцию ad, которые должны вернуться
        Ad ad = new Ad();
        ad.setId(22);
        ad.setUser(user);
        ad.setTitle("MyAd");
        user.setAds(Collections.singletonList(ad));

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/ads/me"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    AdsDto actualAd = objectMapper.readValue(content, AdsDto.class);
                    assertThat(actualAd).isEqualTo(adMap.mapAdsDto(user.getAds()));
                });
    }

    @Test
    @WithMockUser(username = "user")
    public void getAdExtendedTest() throws Exception {
        //заглушка для adRepository.findById
        User user = new User();
        user.setId(1);
        user.setUsername("user");
        Ad ad = new Ad();
        ad.setId(22);
        ad.setUser(user);
        ad.setTitle("MyAd");
        when(adRepository.findById(11)).thenReturn(Optional.of(ad));
        mockMvc.perform(get("/ads/11"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    AdExtendedDto actualExtDto = objectMapper.readValue(content, AdExtendedDto.class);
                    //Проверка, что вернулось объявление, которым замокали репозиторий
                    assertThat(actualExtDto).isEqualTo(adMap.mapAdExtendedDto(ad));
                });
    }
}
