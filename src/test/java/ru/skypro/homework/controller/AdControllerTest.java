package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.service.impl.AdServiceImpl;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdServiceImpl adServiceImpl;

    AdDto adDto1 = AdDto.builder().author(1).image("src/test/resources/pictures/despicable.jpg").pk(100).price(5000).title("Test Ad Title").build();
    AdDto adDto2 = AdDto.builder().author(1).image("src/test/resources/pictures/glass.jpg").pk(100).price(5000).title("Test Ad Title").build();

    AdsDto adsDto = new AdsDto(2, Arrays.asList(adDto1, adDto2));
    ExtendedAdDto extendedAdDto = ExtendedAdDto.builder().pk(1).authorFirstName("Ivan").authorLastName("Ivanov").description("Test description").email("ivan.ivanov@example.com").image("src/test/resources/pictures/fox.jpg").phone("+71234567890").price(10000).title("Test Ad Title").build();
    CreateOrUpdateAdDto createOrUpdateAdDto = CreateOrUpdateAdDto.builder().title("Test Ad Title").price(5000).description("This is a test description").build();

    @Test
    @WithMockUser
    void getAllAds_returnsAdsDto() throws Exception {
        when(adServiceImpl.getAllAds()).thenReturn(adsDto);

        mockMvc.perform((MockMvcRequestBuilders.get("/ads").with(csrf()).accept(MediaType.APPLICATION_JSON))).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));

        Mockito.verify(adServiceImpl, Mockito.times(1)).getAllAds();
    }

    @Test
    @WithMockUser(roles = "USER")
    void addAd_requiresMultipart_andReturnsAdDto() throws Exception {
        when(adServiceImpl.addAd(any(CreateOrUpdateAdDto.class), any())).thenReturn(adDto1);

        MockMultipartFile image = new MockMultipartFile("image", "photo.png", MediaType.IMAGE_PNG_VALUE, "image-bytes".getBytes(StandardCharsets.UTF_8));

        MockMultipartFile properties = new MockMultipartFile("properties", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(createOrUpdateAdDto));

        mockMvc.perform(multipart("/ads").file(properties).file(image).contentType(MediaType.MULTIPART_FORM_DATA).with(csrf())).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
        Mockito.verify(adServiceImpl, Mockito.times(1)).addAd(any(CreateOrUpdateAdDto.class), any());
    }

    @Test
    void getAds_byId_returnsExtendedAdDto() throws Exception {
        when(adServiceImpl.getAdById(ArgumentMatchers.anyInt())).thenReturn(extendedAdDto);

        mockMvc.perform(get("/ads/1").with(csrf())).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
        Mockito.verify(adServiceImpl, Mockito.times(1)).getAdById(1);
    }

    @Test
    @WithMockUser(roles = "USER")
    void removeAd_allowsAdmin() throws Exception {
        Mockito.doNothing().when(adServiceImpl).removeAd(ArgumentMatchers.anyInt());

        mockMvc.perform(delete("/ads/5").with(csrf())).andExpect(status().isOk());

        Mockito.verify(adServiceImpl, Mockito.times(1)).removeAd(5);
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateAds_requiresOwner() throws Exception {
        Mockito.when(adServiceImpl.updateAd(eq(1), any(CreateOrUpdateAdDto.class))).thenReturn(adDto1);

        mockMvc.perform(multipart("/ads/1").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createOrUpdateAdDto)).with(request -> {
            request.setMethod("PATCH");
            return request;
        })).andExpect(status().isOk());
        Mockito.verify(adServiceImpl, Mockito.times(1)).updateAd(eq(1), any(CreateOrUpdateAdDto.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateImage_requiresOwner() throws Exception {
        byte[] bytes = "image-bytes".getBytes();
        MockMultipartFile image = new MockMultipartFile("image", "avatar.png", MediaType.IMAGE_PNG_VALUE, bytes);

        when(adServiceImpl.uploadAdPicture(eq(1), any())).thenReturn(bytes);

        mockMvc.perform(multipart("/ads/1/image").file(image).with(request -> {
            request.setMethod("PATCH");
            return request;
        })).andExpect(status().isOk()).andExpect(content().bytes(bytes));

        Mockito.verify(adServiceImpl, Mockito.times(1)).uploadAdPicture(eq(1), any());
    }
}