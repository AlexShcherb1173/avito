package ru.avito.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import ru.avito.dto.ad.AdDto;
import ru.avito.dto.ad.AdsResponse;
import ru.avito.dto.ad.CreateOrUpdateAdRequest;
import ru.avito.dto.ad.ExtendedAdDto;
import ru.avito.dto.ad.ImageResponse;
import ru.avito.entity.Ad;
import ru.avito.entity.Role;
import ru.avito.entity.User;
import ru.avito.exception.ForbiddenException;
import ru.avito.exception.NotFoundException;
import ru.avito.mapper.AdMapper;
import ru.avito.repository.AdRepository;
import ru.avito.repository.UserRepository;
import ru.avito.security.SecurityUtils;
import ru.avito.service.impl.AdServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdServiceTest {

    @Mock
    private AdRepository adRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdMapper adMapper;

    @Mock
    private ImageService imageService;

    @Mock
    private AccessService accessService;

    @InjectMocks
    private AdServiceImpl adService;

    private User user;
    private User admin;
    private Ad ad;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .email("user@example.com")
                .firstName("Ivan")
                .lastName("Ivanov")
                .phone("+79990000001")
                .role(Role.USER)
                .build();

        admin = User.builder()
                .id(2)
                .email("admin@example.com")
                .firstName("Admin")
                .lastName("User")
                .phone("+79990000002")
                .role(Role.ADMIN)
                .build();

        ad = Ad.builder()
                .id(10)
                .title("Old ad title")
                .price(10000)
                .description("Old ad description")
                .image("/images/ads/10/old.jpg")
                .author(user)
                .build();
    }

    @Test
    void shouldGetAllAds() {
        Ad secondAd = Ad.builder()
                .id(11)
                .title("Second ad")
                .price(20000)
                .description("Second ad description")
                .author(user)
                .build();

        AdDto firstDto = new AdDto();
        firstDto.setPk(10);
        firstDto.setTitle("Old ad title");

        AdDto secondDto = new AdDto();
        secondDto.setPk(11);
        secondDto.setTitle("Second ad");

        when(adRepository.findAll()).thenReturn(List.of(ad, secondAd));
        when(adMapper.toDto(ad)).thenReturn(firstDto);
        when(adMapper.toDto(secondAd)).thenReturn(secondDto);

        AdsResponse result = adService.getAllAds();

        assertThat(result.getCount()).isEqualTo(2);
        assertThat(result.getResults()).hasSize(2);
        verify(adRepository).findAll();
        verify(adMapper).toDto(ad);
        verify(adMapper).toDto(secondAd);
    }

    @Test
    void shouldReturnExtendedAdById() {
        ExtendedAdDto dto = new ExtendedAdDto(
                10,
                "Old ad title",
                "Old ad description",
                10000,
                "/images/ads/10/old.jpg",
                1,
                "Ivan",
                "Ivanov",
                "user@example.com",
                "+79990000001"
        );
        dto.setPk(10);
        dto.setTitle("Old ad title");

        when(adRepository.findById(10)).thenReturn(Optional.of(ad));
        when(adMapper.toExtendedDto(ad)).thenReturn(dto);

        ExtendedAdDto result = adService.getAdById(10);

        assertThat(result.getPk()).isEqualTo(10);
        assertThat(result.getTitle()).isEqualTo("Old ad title");
        verify(adRepository).findById(10);
        verify(adMapper).toExtendedDto(ad);
    }

    @Test
    void shouldThrowWhenAdNotFound() {
        when(adRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> adService.getAdById(999));
    }

    @Test
    void shouldCreateAd() {
        CreateOrUpdateAdRequest request = new CreateOrUpdateAdRequest();
        request.setTitle("Created ad title");
        request.setPrice(25000);
        request.setDescription("Created ad description");

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "ad.jpg",
                "image/jpeg",
                "content".getBytes()
        );

        Ad savedWithoutImage = Ad.builder()
                .id(100)
                .title("Created ad title")
                .price(25000)
                .description("Created ad description")
                .author(user)
                .image(null)
                .build();

        Ad savedWithImage = Ad.builder()
                .id(100)
                .title("Created ad title")
                .price(25000)
                .description("Created ad description")
                .author(user)
                .image("/images/ads/100/ad.jpg")
                .build();

        AdDto mappedDto = new AdDto();
        mappedDto.setPk(100);
        mappedDto.setTitle("Created ad title");
        mappedDto.setPrice(25000);
        mappedDto.setImage("/images/ads/100/ad.jpg");

        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUsername).thenReturn("user@example.com");

            when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
            when(adRepository.save(any(Ad.class)))
                    .thenReturn(savedWithoutImage)
                    .thenReturn(savedWithImage);
            when(imageService.saveAdImage(100, image)).thenReturn("/images/ads/100/ad.jpg");
            when(adMapper.toDto(savedWithImage)).thenReturn(mappedDto);

            AdDto result = adService.createAd(request, image);

            ArgumentCaptor<Ad> captor = ArgumentCaptor.forClass(Ad.class);
            verify(adRepository, times(2)).save(captor.capture());

            Ad firstSaved = captor.getAllValues().get(0);
            assertThat(firstSaved.getTitle()).isEqualTo("Created ad title");
            assertThat(firstSaved.getPrice()).isEqualTo(25000);
            assertThat(firstSaved.getDescription()).isEqualTo("Created ad description");
            assertThat(firstSaved.getAuthor()).isEqualTo(user);

            verify(imageService).saveAdImage(100, image);

            assertThat(result.getPk()).isEqualTo(100);
            assertThat(result.getTitle()).isEqualTo("Created ad title");
            assertThat(result.getImage()).isEqualTo("/images/ads/100/ad.jpg");
        }
    }

    @Test
    void shouldThrowWhenCreateAdImageIsMissing() {
        CreateOrUpdateAdRequest request = new CreateOrUpdateAdRequest();
        request.setTitle("Created ad title");
        request.setPrice(25000);
        request.setDescription("Created ad description");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adService.createAd(request, null)
        );

        assertThat(exception.getMessage()).isEqualTo("Image is required");
    }

    @Test
    void shouldThrowWhenCreateAdImageMimeTypeIsInvalid() {
        CreateOrUpdateAdRequest request = new CreateOrUpdateAdRequest();
        request.setTitle("Created ad title");
        request.setPrice(25000);
        request.setDescription("Created ad description");

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "file.txt",
                "text/plain",
                "content".getBytes()
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adService.createAd(request, image)
        );

        assertThat(exception.getMessage()).isEqualTo("Only image files are allowed");
    }

    @Test
    void shouldUpdateAd() {
        CreateOrUpdateAdRequest request = new CreateOrUpdateAdRequest();
        request.setTitle("Updated ad title");
        request.setPrice(33000);
        request.setDescription("Updated ad description");

        AdDto mappedDto = new AdDto();
        mappedDto.setPk(10);
        mappedDto.setTitle("Updated ad title");
        mappedDto.setPrice(33000);

        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUsername).thenReturn("user@example.com");

            when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
            when(adRepository.findById(10)).thenReturn(Optional.of(ad));
            when(adRepository.save(ad)).thenReturn(ad);
            when(adMapper.toDto(ad)).thenReturn(mappedDto);

            AdDto result = adService.updateAd(10, request);

            verify(accessService).checkAdEditAccess(user, ad);
            verify(adRepository).save(ad);

            assertThat(ad.getTitle()).isEqualTo("Updated ad title");
            assertThat(ad.getPrice()).isEqualTo(33000);
            assertThat(ad.getDescription()).isEqualTo("Updated ad description");
            assertThat(result.getTitle()).isEqualTo("Updated ad title");
        }
    }

    @Test
    void shouldDeleteAd() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUsername).thenReturn("user@example.com");

            when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
            when(adRepository.findById(10)).thenReturn(Optional.of(ad));

            adService.deleteAd(10);

            verify(accessService).checkAdDeleteAccess(user, ad);
            verify(imageService).deleteImageIfExists("/images/ads/10/old.jpg");
            verify(adRepository).delete(ad);
        }
    }

    @Test
    void shouldGetMyAds() {
        Ad secondAd = Ad.builder()
                .id(11)
                .title("Second ad")
                .price(20000)
                .description("Second ad description")
                .author(user)
                .build();

        AdDto firstDto = new AdDto();
        firstDto.setPk(10);
        firstDto.setTitle("Old ad title");

        AdDto secondDto = new AdDto();
        secondDto.setPk(11);
        secondDto.setTitle("Second ad");

        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUsername).thenReturn("user@example.com");

            when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
            when(adRepository.findAllByAuthorId(1)).thenReturn(List.of(ad, secondAd));
            when(adMapper.toDto(ad)).thenReturn(firstDto);
            when(adMapper.toDto(secondAd)).thenReturn(secondDto);

            AdsResponse result = adService.getMyAds();

            assertThat(result.getCount()).isEqualTo(2);
            assertThat(result.getResults()).hasSize(2);
            verify(adRepository).findAllByAuthorId(1);
        }
    }

    @Test
    void shouldUpdateAdImage() {
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "new.jpg",
                "image/jpeg",
                "content".getBytes()
        );

        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUsername).thenReturn("user@example.com");

            when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
            when(adRepository.findById(10)).thenReturn(Optional.of(ad));
            when(imageService.saveAdImage(10, image)).thenReturn("/images/ads/10/new.jpg");

            ImageResponse result = adService.updateAdImage(10, image);

            verify(accessService).checkAdImageAccess(user, ad);
            verify(imageService).deleteImageIfExists("/images/ads/10/old.jpg");
            verify(imageService).saveAdImage(10, image);
            verify(adRepository).save(ad);

            assertThat(ad.getImage()).isEqualTo("/images/ads/10/new.jpg");
            assertThat(result.getUrl()).isEqualTo("/images/ads/10/new.jpg");
        }
    }

    @Test
    void shouldThrowWhenAuthenticatedUserEmailIsBlank() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUsername).thenReturn("");

            MockMultipartFile image = new MockMultipartFile(
                    "image",
                    "ad.jpg",
                    "image/jpeg",
                    "content".getBytes()
            );

            CreateOrUpdateAdRequest request = new CreateOrUpdateAdRequest();
            request.setTitle("Created ad title");
            request.setPrice(25000);
            request.setDescription("Created ad description");

            assertThrows(ForbiddenException.class, () -> adService.createAd(request, image));
        }
    }

    @Test
    void shouldThrowWhenAuthenticatedUserNotFound() {
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentUsername).thenReturn("missing@example.com");
            when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> adService.getMyAds());
        }
    }
}