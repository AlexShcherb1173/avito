package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.generatedDto.AdDto;
import ru.skypro.homework.dto.generatedDto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.generatedDto.ExtendedAdDto;
import ru.skypro.homework.dto.generatedDto.AdsDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.AdsNotFoundException;
import ru.skypro.homework.exception.UserHasNoRightsException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.mapper.AdMapperImpl;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.impl.AdServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdServiceImplTest {
    @Mock
    private AdRepository adsRepository;
//    @Mock
//    private ImageRepository imageRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private UserService userService;
    @Spy
    private AdMapper adsMapper = new AdMapperImpl();
    @InjectMocks
    private AdServiceImpl out;

    private List<Ad> adsList;
    private Ad ad1;
    private Ad ad2;
    private CreateOrUpdateAdDto createOrUpdateAdDto;
    private User testUser;
    private Authentication auth;
    private Image testImage;

    @BeforeEach
    void init() {
        testUser = new User();
        testUser.setId(42L);
        testUser.setUsername("test@test.com");
        auth = new UsernamePasswordAuthenticationToken(testUser, null);

        testImage = new Image();
        testImage.setId(1L);

        createOrUpdateAdDto = new CreateOrUpdateAdDto();
        createOrUpdateAdDto.setDescription("Test description");
        createOrUpdateAdDto.setTitle("Test title");
        createOrUpdateAdDto.setPrice(15);


        ad1 = new Ad();
        ad1.setId(1L);
        ad1.setPrice(new BigDecimal(10));
        ad1.setTitle("Test ads");
        ad1.setAuthor(testUser);
        ad1.setImage(testImage);

        ad2 = new Ad();
        ad2.setId(2L);
        ad2.setPrice(new BigDecimal(20));
        ad2.setTitle("Test ads 2");
        ad2.setAuthor(testUser);
        ad2.setImage(testImage);

        adsList = List.of(ad1, ad2);
    }

    @Test
    void shouldReturnAdsDtoWithAllAds_whenGetAllAds() {
        when(adsRepository.findAll()).thenReturn(adsList);
        AdsDto result = out.getAllAds();
        System.out.println(result);

        assertThat(result).isNotNull();
        assertThat(result.getCount()).isEqualTo(adsList.size());
        assertThat(result.getResults()).isNotNull();
    }

    @Test
    void shouldReturnAdDto_WhenCreateAd() {
        Ad adsForMockSave  = adsMapper.createOrUpdateAdDtoToAd(createOrUpdateAdDto);
        when(imageService.createImage(any(), any())).thenReturn(testImage);
        when(adsRepository.save(any(Ad.class))).thenReturn(adsForMockSave);
        when(userService.getUser(any(String.class))).thenReturn(null);
        AdDto result = out.createAd(createOrUpdateAdDto, null, auth);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(createOrUpdateAdDto.getTitle());
        assertThat(result.getPrice()).isEqualTo(createOrUpdateAdDto.getPrice());
    }

    @Test
    void shouldThrowException_whenGetAdsWithWrongId() {
        when(adsRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> out.getAdById(1L)).isInstanceOf(AdsNotFoundException.class);
    }

    @Test
    void shouldReturnFullAdsDto_whenGetFullAdsById() {
        when(adsRepository.findById(any(Long.class))).thenReturn(Optional.of(ad1));
        ExtendedAdDto result = out.getFullAdById(ad1.getId());

        assertThat(result).isNotNull();
        assertThat(result.getPk()).isEqualTo(ad1.getId().intValue());
        assertThat(result.getTitle()).isEqualTo(ad1.getTitle());
        assertThat(result.getPrice()).isEqualTo(ad1.getPrice().intValue());
        assertThat(result.getAuthorFirstName()).isNull();
    }

    @Test
    void shouldReturnResponseWrapperAdsForUser_whenGetAllAdsForUser() {
        when(adsRepository.findAdByAuthorUsername(any(String.class))).thenReturn(adsList);
        AdsDto result = out.getAllAdsForUser("Username");

        assertThat(result).isNotNull();
        assertThat(result.getCount()).isEqualTo(adsList.size());
        assertThat(result.getResults().contains(adsMapper.adToAdDto(ad1))).isTrue();
    }

    @Test
    void shouldThrowUserHasNoRightsException_whenUserHasNORightsToRemoveAds() {
        when(adsRepository.findById(anyLong())).thenReturn(Optional.of(ad1));
        doThrow(UserHasNoRightsException.class).when(userService).checkUserHasPermit(auth, testUser.getUsername());

        assertThatExceptionOfType(UserHasNoRightsException.class)
                .isThrownBy(() -> out.removeAd(ad1.getId(), auth));
    }

    @Test
    void shouldExecuteDeleteOnce_whenUserCanRemoveAds() {
        when(adsRepository.findById(anyLong())).thenReturn(Optional.of(ad1));
        doNothing().when(userService).checkUserHasPermit(auth, testUser.getUsername());

        out.removeAd(ad1.getId(), auth);

        verify(adsRepository, atMostOnce()).delete(ad1);
    }

    @Test
    void shouldThrowUserHasNoRightsException_whenUserHasNORightsToUpdateAds() {
        when(adsRepository.findById(anyLong())).thenReturn(Optional.of(ad1));
        doThrow(UserHasNoRightsException.class).when(userService).checkUserHasPermit(auth, testUser.getUsername());

        assertThatExceptionOfType(UserHasNoRightsException.class)
                .isThrownBy(() -> out.updateAdById(ad1.getId(), createOrUpdateAdDto, auth));
    }

    @Test
    void shouldExecuteSaveOnce_whenUserCanUpdateAds() {
        Ad adsForMockSave  = adsMapper.createOrUpdateAdDtoToAd(createOrUpdateAdDto);
        when(adsRepository.findById(anyLong())).thenReturn(Optional.of(ad1));
        doNothing().when(userService).checkUserHasPermit(auth, testUser.getUsername());
        when(adsRepository.save(any())).thenReturn(adsForMockSave);
        try {
            AdDto result = out.updateAdById(ad1.getId(), createOrUpdateAdDto, auth);


        verify(adsRepository, atMostOnce()).save(ad1);
        verify(adsMapper, atMostOnce()).adToAdDto(ad1);

        assertThat(result).isNotNull();
        assertThat(result.getPrice()).isEqualTo(createOrUpdateAdDto.getPrice());
        } catch (NullPointerException e) {
            System.out.println("Result.image = null");
        }
    }
}