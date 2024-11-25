package ru.skypro.homework.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.skypro.homework.dto.listing.CreateOrUpdateListing;
import ru.skypro.homework.dto.listing.ExtendedListingDTO;
import ru.skypro.homework.dto.listing.ListingDTO;
import ru.skypro.homework.dto.listing.ListingsDTO;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.service.impl.ImageServiceImpl;
import ru.skypro.homework.service.impl.ListingsServiceImpl;

import java.io.IOException;
import java.util.Collections;

import static java.lang.reflect.Array.get;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ListingControllerTest {

    @Mock
    private ListingsServiceImpl listingService;

    @Mock
    private ImageServiceImpl imageService;

    @InjectMocks
    private ListingsController listingsController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(listingsController).build();
    }

    @Test
    public void testGetAllListings() throws Exception {
        // Arrange
        ListingDTO listingDTO = new ListingDTO(1L, "image.png", 1L, 100, "Title");
        ListingsDTO listingsDTO = new ListingsDTO(1, Collections.singletonList(listingDTO));

        when(listingService.getAllListings()).thenReturn(listingsDTO.getResults());

        // Act & Assert
        mockMvc.perform((RequestBuilder) get("/ads"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.results[0].title").value("Title"));

        verify(listingService, times(1)).getAllListings();
    }

    @Test
    void addListing() throws IOException {
        CreateOrUpdateListing createOrUpdateListingDTO = new CreateOrUpdateListing();
        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[]{});
        Authentication authentication = mock(Authentication.class);
        ListingDTO listingDTO = new ListingDTO();
        when(listingService.addListing(createOrUpdateListingDTO, image, authentication)).thenReturn(listingDTO);

        ResponseEntity<ListingDTO> response = listingsController.addListing(createOrUpdateListingDTO, image, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(listingDTO, response.getBody());
        verify(listingService, times(1)).addListing(createOrUpdateListingDTO, image, authentication);
    }

    @Test
    void getListing() {
        long id = 1L;
        ExtendedListingDTO extendedListingDTO = new ExtendedListingDTO();
        when(listingService.getListing(id)).thenReturn(extendedListingDTO);

        ResponseEntity<ExtendedListingDTO> response = listingsController.getListing(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(extendedListingDTO, response.getBody());
        verify(listingService, times(1)).getListing(id);
    }

    @Test
    void deleteListing() throws AccessDeniedException, java.nio.file.AccessDeniedException {
        long id = 1L;
        Authentication authentication = mock(Authentication.class);
        doNothing().when(listingService).deleteListing(id, authentication);

        ResponseEntity<?> response = listingsController.deleteListing(id, authentication);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(listingService, times(1)).deleteListing(id, authentication);
    }

    @Test
    void updateListingInformation() throws AccessDeniedException, java.nio.file.AccessDeniedException {
        long id = 1L;
        CreateOrUpdateListing createOrUpdateListing = new CreateOrUpdateListing();
        Authentication authentication = mock(Authentication.class);
        ListingDTO listingDTO = new ListingDTO();
        when(listingService.updateListing(id, createOrUpdateListing, authentication)).thenReturn(listingDTO);

        ResponseEntity<ListingDTO> response = listingsController.updateListingInformation(id, createOrUpdateListing, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(listingDTO, response.getBody());
        verify(listingService, times(1)).updateListing(id, createOrUpdateListing, authentication);
    }

    @Test
    void getListingsFromAuthorizedUser() {
        Authentication authentication = mock(Authentication.class);
        ListingsDTO listingsDTO = new ListingsDTO();
        when(listingService.getListingsMe(authentication)).thenReturn(listingsDTO);

        ResponseEntity<ListingsDTO> response = listingsController.getListingsFromAuthorizedUser(authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(listingsDTO, response.getBody());
        verify(listingService, times(1)).getListingsMe(authentication);
    }

    @Test
    void updateImage() throws IOException {
        long id = 1L;
        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[]{});
        Authentication authentication = mock(Authentication.class);
        doNothing().when(listingService).updateListingImage(id, image, authentication);

        ResponseEntity<?> response = listingsController.updateImage(id, image, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(listingService, times(1)).updateListingImage(id, image, authentication);
    }


    @Test
    void getListingsImage() {
        long id = 1L;
        byte[] imageData = new byte[]{1, 2, 3};
        when(imageService.getImage(id)).thenReturn(imageData);

        ResponseEntity<byte[]> response = listingsController.getListingsImage(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(imageData, response.getBody());
        verify(imageService, times(1)).getImage(id);
    }
}