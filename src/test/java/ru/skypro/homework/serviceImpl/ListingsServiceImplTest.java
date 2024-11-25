package ru.skypro.homework.serviceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;
import ru.skypro.homework.config.UserDetailsImpl;
import ru.skypro.homework.dto.listing.CreateOrUpdateListing;
import ru.skypro.homework.dto.listing.ExtendedListingDTO;
import ru.skypro.homework.dto.listing.ListingDTO;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.Listing;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.ListingMapper;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.ListingRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.impl.ListingsServiceImpl;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListingsServiceImplTest {

    @Mock
    private ListingRepository listingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private ListingMapper listingMapper;
    @Mock
    private ImageService imageService;
    @InjectMocks
    private ListingsServiceImpl listingsService;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    public void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testGetAllListings() {

        List<Listing> listings = List.of(mock(Listing.class), mock(Listing.class));
        when(listingRepository.findAll()).thenReturn(listings);


        List<ListingDTO> result = listingsService.getAllListings();


        verify(listingRepository).findAll();
        verify(listingMapper, times(2)).listingToListingDTO(any(Listing.class));
        assertEquals(2, result.size());
    }

    @Test
    public void testAddListing() throws IOException {

        CreateOrUpdateListing createOrUpdateListing = mock(CreateOrUpdateListing.class);
        MultipartFile image = mock(MultipartFile.class);
        Authentication authentication = mock(Authentication.class);
        User user = mock(User.class);
        Listing listing = mock(Listing.class);
        Image savedImage = mock(Image.class);
        ListingDTO listingDTO = mock(ListingDTO.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);

        when(authentication.getName()).thenReturn("testuser");

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUser()).thenReturn(user);

        when(listingMapper.createOrUpdateListingToListing(createOrUpdateListing)).thenReturn(listing);
        when(listingRepository.save(listing)).thenReturn(listing);
        when(imageService.uploadImage(anyLong(), eq(image))).thenReturn(savedImage);
        when(listingMapper.listingToListingDTO(listing)).thenReturn(listingDTO);


        ListingDTO result = listingsService.addListing(createOrUpdateListing, image, authentication);

        verify(imageService).uploadImage(anyLong(), eq(image));
        assertEquals(listingDTO, result);
    }

    @Test
    public void testGetListing() {

        long id = 1L;
        Listing listing = mock(Listing.class);
        ExtendedListingDTO extendedListingDTO = mock(ExtendedListingDTO.class);


        when(listingRepository.findById(id)).thenReturn(Optional.of(listing));
        when(listingMapper.toExtendedListing(listing)).thenReturn(extendedListingDTO);


        ExtendedListingDTO result = listingsService.getListing(id);


        verify(listingRepository).findById(id);
        verify(listingMapper).toExtendedListing(listing);
        assertEquals(extendedListingDTO, result);
    }

    @Test
    public void testGetListingNotFound() {

        long id = 1L;
        when(listingRepository.findById(id)).thenReturn(Optional.empty());


        assertThrows(NotFoundException.class, () -> listingsService.getListing(id));
    }

    @Test
    public void testDeleteListing() throws AccessDeniedException {

        long id = 1L;
        Listing listing = mock(Listing.class);
        Image image = mock(Image.class);
        Authentication authentication = mock(Authentication.class);
        User user = mock(User.class);


        when(listingRepository.findById(id)).thenReturn(Optional.of(listing));
        when(listing.getUser()).thenReturn(user);
        when(user.getEmail()).thenReturn("testuser");
        when(authentication.getName()).thenReturn("testuser");
        when(listing.getImage()).thenReturn(image);


        listingsService.deleteListing(id, authentication);


        verify(listingRepository).findById(id);
        verify(commentRepository).deleteCommentsByListingId(id);
        verify(imageRepository).deleteById(image.getId());
        verify(listingRepository).deleteById(id);
    }

    @Test
    public void testCheckPermitAccessDenied() {

        Listing listing = mock(Listing.class);
        User user = mock(User.class);
        Authentication authentication = mock(Authentication.class);


        when(listing.getUser()).thenReturn(user);
        when(user.getEmail()).thenReturn("owner@example.com");
        when(authentication.getName()).thenReturn("user@example.com");
        when(authentication.getAuthorities()).thenReturn(Collections.emptySet());


        assertThrows(AccessDeniedException.class, () -> listingsService.checkPermit(listing, authentication));
    }

}
