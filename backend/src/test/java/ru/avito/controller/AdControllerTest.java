package ru.avito.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import ru.avito.dto.ad.AdDto;
import ru.avito.dto.ad.AdsResponse;
import ru.avito.dto.ad.CreateOrUpdateAdRequest;
import ru.avito.dto.ad.ExtendedAdDto;
import ru.avito.dto.ad.ImageResponse;
import ru.avito.exception.ForbiddenException;
import ru.avito.exception.GlobalExceptionHandler;
import ru.avito.exception.NotFoundException;
import ru.avito.service.AdService;
import ru.avito.util.ImagePathUtils;

import java.nio.file.Paths;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({GlobalExceptionHandler.class, AdControllerTest.TestImagePathConfig.class})
class AdControllerTest {

    @TestConfiguration
    static class TestImagePathConfig {
        @Bean
        @Primary
        ImagePathUtils imagePathUtils() {
            ImagePathUtils mock = Mockito.mock(ImagePathUtils.class);
            Mockito.when(mock.getRootDir()).thenReturn(Paths.get("target/test-images"));
            return mock;
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdService adService;

    @Test
    void getAllAdsShouldReturn200() throws Exception {
        AdDto first = new AdDto();
        first.setPk(1);
        first.setAuthor(10);
        first.setTitle("First ad");
        first.setPrice(1000);
        first.setImage("/images/ads/1/image.jpg");

        AdDto second = new AdDto();
        second.setPk(2);
        second.setAuthor(11);
        second.setTitle("Second ad");
        second.setPrice(2000);
        second.setImage("/images/ads/2/image.jpg");

        when(adService.getAllAds()).thenReturn(new AdsResponse(2, List.of(first, second)));

        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.results[0].pk").value(1))
                .andExpect(jsonPath("$.results[0].title").value("First ad"))
                .andExpect(jsonPath("$.results[1].pk").value(2))
                .andExpect(jsonPath("$.results[1].title").value("Second ad"));
    }

    @Test
    void getAdByIdShouldReturn200() throws Exception {
        ExtendedAdDto dto = new ExtendedAdDto(
                1,
                "First ad",
                "Ad description",
                1000,
                "/images/ads/1/image.jpg",
                10,
                "Ivan",
                "Ivanov",
                "user@example.com",
                "+79990000001"
        );

        when(adService.getAdById(1)).thenReturn(dto);

        mockMvc.perform(get("/ads/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(1))
                .andExpect(jsonPath("$.title").value("First ad"))
                .andExpect(jsonPath("$.description").value("Ad description"))
                .andExpect(jsonPath("$.price").value(1000))
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.phone").value("+79990000001"));
    }

    @Test
    void getAdByIdShouldReturn404WhenAdNotFound() throws Exception {
        when(adService.getAdById(999)).thenThrow(new NotFoundException("Ad not found"));

        mockMvc.perform(get("/ads/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Ad not found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void getMyAdsShouldReturn200() throws Exception {
        AdDto own = new AdDto();
        own.setPk(1);
        own.setAuthor(10);
        own.setTitle("Own ad");
        own.setPrice(1000);

        when(adService.getMyAds()).thenReturn(new AdsResponse(1, List.of(own)));

        mockMvc.perform(get("/ads/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.results[0].pk").value(1))
                .andExpect(jsonPath("$.results[0].title").value("Own ad"));
    }

    @Test
    void createAdShouldReturn201WhenMultipartIsValid() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "ad.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-image-content".getBytes()
        );

        MockMultipartFile properties = new MockMultipartFile(
                "properties",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                """
                {
                  "title": "Created ad",
                  "price": 25000,
                  "description": "Created ad description"
                }
                """.getBytes()
        );

        AdDto created = new AdDto();
        created.setPk(100);
        created.setAuthor(10);
        created.setTitle("Created ad");
        created.setPrice(25000);
        created.setImage("/images/ads/100/ad.jpg");

        when(adService.createAd(any(CreateOrUpdateAdRequest.class), any())).thenReturn(created);

        mockMvc.perform(multipart("/ads")
                        .file(image)
                        .file(properties))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pk").value(100))
                .andExpect(jsonPath("$.title").value("Created ad"))
                .andExpect(jsonPath("$.price").value(25000))
                .andExpect(jsonPath("$.image").value("/images/ads/100/ad.jpg"));
    }

    @Test
    void createAdShouldReturn400WhenImagePartIsMissing() throws Exception {
        MockMultipartFile properties = new MockMultipartFile(
                "properties",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                """
                {
                  "title": "Created ad",
                  "price": 25000,
                  "description": "Created ad description"
                }
                """.getBytes()
        );

        mockMvc.perform(multipart("/ads")
                        .file(properties))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Missing request part: image"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void createAdShouldReturn400WhenPayloadIsInvalid() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "ad.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-image-content".getBytes()
        );

        MockMultipartFile properties = new MockMultipartFile(
                "properties",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                """
                {
                  "title": "short",
                  "price": 0,
                  "description": "short"
                }
                """.getBytes()
        );

        mockMvc.perform(multipart("/ads")
                        .file(image)
                        .file(properties))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void updateAdShouldReturn200ForValidRequest() throws Exception {
        CreateOrUpdateAdRequest request = new CreateOrUpdateAdRequest();
        request.setTitle("Updated ad");
        request.setPrice(33000);
        request.setDescription("Updated ad description");

        AdDto updated = new AdDto();
        updated.setPk(1);
        updated.setAuthor(10);
        updated.setTitle("Updated ad");
        updated.setPrice(33000);

        when(adService.updateAd(eq(1), any(CreateOrUpdateAdRequest.class))).thenReturn(updated);

        mockMvc.perform(patch("/ads/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(1))
                .andExpect(jsonPath("$.title").value("Updated ad"))
                .andExpect(jsonPath("$.price").value(33000));
    }

    @Test
    void updateAdShouldReturn403ForForbidden() throws Exception {
        CreateOrUpdateAdRequest request = new CreateOrUpdateAdRequest();
        request.setTitle("Updated ad");
        request.setPrice(33000);
        request.setDescription("Updated ad description");

        when(adService.updateAd(eq(1), any(CreateOrUpdateAdRequest.class)))
                .thenThrow(new ForbiddenException("You cannot edit this ad"));

        mockMvc.perform(patch("/ads/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("You cannot edit this ad"))
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    void updateAdShouldReturn404WhenAdNotFound() throws Exception {
        CreateOrUpdateAdRequest request = new CreateOrUpdateAdRequest();
        request.setTitle("Updated ad");
        request.setPrice(33000);
        request.setDescription("Updated ad description");

        when(adService.updateAd(eq(999), any(CreateOrUpdateAdRequest.class)))
                .thenThrow(new NotFoundException("Ad not found"));

        mockMvc.perform(patch("/ads/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Ad not found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deleteAdShouldReturn204ForSuccess() throws Exception {
        doNothing().when(adService).deleteAd(1);

        mockMvc.perform(delete("/ads/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAdShouldReturn403ForForbidden() throws Exception {
        doThrow(new ForbiddenException("You cannot delete this ad"))
                .when(adService)
                .deleteAd(1);

        mockMvc.perform(delete("/ads/1"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("You cannot delete this ad"))
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    void updateAdImageShouldReturn200WhenMultipartIsValid() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "new.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "new-image-content".getBytes()
        );

        when(adService.updateAdImage(eq(1), any())).thenReturn(
                new ImageResponse("/images/ads/1/new.jpg")
        );

        mockMvc.perform(multipart("/ads/1/image")
                        .file(image)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("/images/ads/1/new.jpg"));
    }

    @Test
    void updateAdImageShouldReturn400WhenImagePartIsMissing() throws Exception {
        mockMvc.perform(multipart("/ads/1/image")
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Missing request part: image"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void updateAdImageShouldReturn403ForForbidden() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "new.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "new-image-content".getBytes()
        );

        when(adService.updateAdImage(eq(1), any()))
                .thenThrow(new ForbiddenException("You cannot update image for this ad"));

        mockMvc.perform(multipart("/ads/1/image")
                        .file(image)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("You cannot update image for this ad"))
                .andExpect(jsonPath("$.status").value(403));
    }
}