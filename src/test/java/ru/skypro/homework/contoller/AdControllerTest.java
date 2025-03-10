package ru.skypro.homework.contoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.service.AdService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdService adService;

    @Test
    @WithMockUser
    void getAllAds_ShouldReturnAds() throws Exception {
        AdsDTO adsDTO = new AdsDTO(1, List.of(new AdDTO()));
        when(adService.getAllAds()).thenReturn(adsDTO);

        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1));
    }

    @Test
    @WithMockUser
    void addAd_Authorized_ShouldReturnCreatedAd() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "image-content".getBytes());
        CreateOrUpdateAdDTO properties = new CreateOrUpdateAdDTO();
        AdDTO adDTO = new AdDTO();

        when(adService.addAd(any(CreateOrUpdateAdDTO.class), any(MultipartFile.class))).thenReturn(adDTO);

        mockMvc.perform(multipart("/ads")
                        .file(image)
                        .param("properties", new ObjectMapper().writeValueAsString(properties)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void getAd_ShouldReturnAd() throws Exception {
        AdDTO adDTO = new AdDTO();
        when(adService.getAdById(1)).thenReturn(adDTO);

        mockMvc.perform(get("/ads/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteAd_Admin_ShouldReturnNoContent() throws Exception {
        doNothing().when(adService).deleteAd(1);

        mockMvc.perform(delete("/ads/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateAd_Authorized_ShouldReturnUpdatedAd() throws Exception {
        CreateOrUpdateAdDTO updateAdDTO = new CreateOrUpdateAdDTO();
        AdDTO updatedAdDTO = new AdDTO();
        when(adService.updateAd(eq(1), any(CreateOrUpdateAdDTO.class))).thenReturn(updatedAdDTO);

        mockMvc.perform(patch("/ads/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateAdDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getMyAds_Authorized_ShouldReturnAds() throws Exception {
        AdsDTO adsDTO = new AdsDTO(1, List.of(new AdDTO()));
        when(adService.getMyAds()).thenReturn(adsDTO);

        mockMvc.perform(get("/ads/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1));
    }

    @Test
    @WithMockUser
    void updateAdImage_Authorized_ShouldReturnOk() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "image-content".getBytes());
        doNothing().when(adService).updateAdImage(eq(1), any(MultipartFile.class));

        mockMvc.perform(multipart("/ads/1/image").file(image))
                .andExpect(status().isOk());
    }
}
