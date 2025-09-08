package ru.skypro.homework.service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;


import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.ImageEntity;

import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.ImageRepository;

import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.AdServiceImpl;
import ru.skypro.homework.service.impl.CommentServiceImpl;
import ru.skypro.homework.service.impl.UserServiceImpl;


import java.io.IOException;
import java.util.*;

import static org.mockito.Mockito.*;
import static ru.skypro.homework.service.ServiceTestFabric.*;



@SpringBootTest
public class AdServiceTest {


    @Mock
    private AdRepository adRepository;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private AdMapper adMapper;
    @Mock
    private CommentServiceImpl commentService;
    private AdServiceImpl adService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        adService = new AdServiceImpl(adRepository, imageRepository, userRepository, userService, adMapper, commentService);
    }

    @Test
    public void testRemoveAd() {

        AdEntity ad = new AdEntity();
        ad.setImage(new ImageEntity());

        when(adRepository.findById(TEST_ID)).thenReturn(Optional.of(ad));

        adService.removeAd(TEST_ID);

        verify(imageRepository).delete(ad.getImage());
        verify(commentService).deleteCommentsByAdId(TEST_ID);
        verify(adRepository).deleteById(TEST_ID);
    }

    @Test
    public void testUpdateAdImage() throws IOException {
        MultipartFile image = new MockMultipartFile(TEST_FILE_NAME, TEST_FILE_CONTENT);
        when(adRepository.findById(TEST_ID)).thenReturn(Optional.of(new AdEntity()));
        when(imageRepository.findById(TEST_ID)).thenReturn(Optional.of(new ImageEntity()));
        when(imageRepository.save(any(ImageEntity.class))).thenReturn(new ImageEntity());
        adService.updateAdImage(TEST_ID, image);

        verify(imageRepository).save(any(ImageEntity.class));
    }

    @Test
    public void testGetAllAds() {
        List<AdEntity> mockAds = new ArrayList<>();
        AdEntity ad1 = new AdEntity();
        ad1.setId(TEST_ID);
        ad1.setTitle(TEST_TITLE);
        ad1.setDescription(TEST_DESCRIPTION);
        mockAds.add(ad1);

        AdEntity ad2 = new AdEntity();
        ad2.setId(TEST_ID);
        ad2.setTitle(TEST_TITLE);
        ad2.setDescription(TEST_DESCRIPTION);
        mockAds.add(ad2);

        List<AdDto> expectedAdsDtoList = new ArrayList<>();
        AdDto expectedAdDto1 = new AdDto();
        expectedAdDto1.setTitle(ad1.getTitle());
        expectedAdsDtoList.add(expectedAdDto1);

        AdDto expectedAdDto2 = new AdDto();
        expectedAdDto2.setTitle(ad2.getTitle());
        expectedAdsDtoList.add(expectedAdDto2);

        AdsDto expectedResponse = new AdsDto();
        expectedResponse.setCount(mockAds.size());
        expectedResponse.setResults(expectedAdsDtoList);

        when(adRepository.findAll()).thenReturn(mockAds);
        when(adMapper.adListToAdsDtoList(mockAds)).thenReturn(expectedAdsDtoList);
        AdsDto result = adService.getAllAds();

        assertNotNull(result);
        assertNotNull(result.getResults());
        assertEquals(mockAds.size(), result.getCount());
        assertEquals(expectedResponse, result);
    }

    @Test
    public void testAddAd() throws IOException {
        CreateOrUpdateAdDto createAdsDto = new CreateOrUpdateAdDto();
        createAdsDto.setTitle(TEST_TITLE);

        MockMultipartFile file = new MockMultipartFile("file", TEST_FILE_NAME, TEST_FILE_CONTENT_TYPE, TEST_FILE_CONTENT);

        ImageEntity savedImage = new ImageEntity();
        savedImage.setId(1);
        savedImage.setMediaType(file.getContentType());
        savedImage.setData(file.getBytes());

        UserEntity user = new UserEntity();
        AdDto expectedAdsDto = new AdDto();

        AdEntity ad = new AdEntity();
        ad.setId(1);
        ad.setTitle(createAdsDto.getTitle());

        when(adMapper.toAd(createAdsDto)).thenReturn(ad);
        when(imageRepository.save(savedImage)).thenReturn(savedImage);
        when(userRepository.findByUsername(userService.getCurrentUsername())).thenReturn(Optional.of(user));
        when(adRepository.save(ad)).thenReturn(ad);
        when(adMapper.toAdsDto(ad)).thenReturn(expectedAdsDto);

        AdDto result = adService.addAd(createAdsDto, file);

        assertNotNull(result);
    }


    @Test
    public void testGetAdImageNotFound() {
        when(adRepository.findById(TEST_ID)).thenReturn(Optional.empty());
        ImageEntity result = adService.getAdImage(TEST_ID);
        assertNull(result);
    }

    @Test
    public void testGetAdImage() {
        Integer adId = 1;
        ImageEntity expectedImage = new ImageEntity();
        AdEntity ad = new AdEntity();
        ad.setImage(expectedImage);
        when(adRepository.findById(adId)).thenReturn(Optional.of(ad));
        ImageEntity result = adService.getAdImage(adId);
        Assertions.assertEquals(expectedImage, result);
    }


    @Test
    public void testGetAdsNotFound() {
        when(adRepository.findById(TEST_ID)).thenReturn(Optional.empty());
        ExtendedAdDto result = adService.getAds(TEST_ID);
        assertNull(result);
    }

    @Test
    public void testGetAds() {

        AdEntity ad = new AdEntity();
        ad.setId(TEST_ID);
        ad.setTitle(TEST_TITLE);
        ad.setDescription(TEST_DESCRIPTION);

        ExtendedAdDto expectedFullAdsDto = new ExtendedAdDto();
        expectedFullAdsDto.setPk(ad.getId());
        expectedFullAdsDto.setTitle(ad.getTitle());
        expectedFullAdsDto.setDescription(ad.getDescription());

        when(adRepository.findById(TEST_ID)).thenReturn(Optional.of(ad));
        when(adMapper.toFullAdsDto(ad)).thenReturn(expectedFullAdsDto);

        ExtendedAdDto result = adService.getAds(TEST_ID);

        Assertions.assertEquals(expectedFullAdsDto.getPk(), result.getPk());
        Assertions.assertEquals(expectedFullAdsDto.getTitle(), result.getTitle());
        Assertions.assertEquals(expectedFullAdsDto.getDescription(), result.getDescription());
    }


    @Test
    public void testGetAdsMe() {


        UserEntity user = new UserEntity();
        user.setId(1);
        user.setUsername(TEST_USERNAME);

        AdEntity ad1 = new AdEntity();
        ad1.setId(1);
        ad1.setTitle(TEST_TITLE + " 1");

        AdEntity ad2 = new AdEntity();
        ad2.setId(2);
        ad2.setTitle(TEST_TITLE + " 2");

        List<AdEntity> mockAds = new ArrayList<>();
        mockAds.add(ad1);
        mockAds.add(ad2);

        List<AdDto> expectedAdsDtoList = new ArrayList<>();
        AdDto expectedAdDto1 = new AdDto();
        expectedAdDto1.setTitle(ad1.getTitle());
        expectedAdsDtoList.add(expectedAdDto1);

        AdDto expectedAdDto2 = new AdDto();
        expectedAdDto2.setTitle(ad2.getTitle());
        expectedAdsDtoList.add(expectedAdDto2);

        AdsDto expectedResponse = new AdsDto();
        expectedResponse.setCount(mockAds.size());
        expectedResponse.setResults(expectedAdsDtoList);

        when(userService.getCurrentUsername()).thenReturn(TEST_USERNAME);
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(user));
        when(adRepository.findAllByUserId(user.getId())).thenReturn(mockAds);
        when(adMapper.adListToAdsDtoList(mockAds)).thenReturn(expectedAdsDtoList);

        AdsDto result = adService.getAdsMe();
        assertNotNull(result);
        assertEquals(mockAds.size(), result.getCount());
        assertEquals(expectedAdsDtoList, result.getResults());
    }

    @Test
    public void testUpdateDto() {

        Integer newPrice = 999;

        AdEntity existingAd = new AdEntity();
        existingAd.setId(TEST_ID);
        existingAd.setTitle("Old Title");
        existingAd.setDescription("Old Description");
        existingAd.setPrice(499);

        CreateOrUpdateAdDto updateProperties = new CreateOrUpdateAdDto();
        updateProperties.setTitle(TEST_TITLE);
        updateProperties.setDescription(TEST_DESCRIPTION);
        updateProperties.setPrice(newPrice);

        AdDto expectedAdsDto = new AdDto();
        expectedAdsDto.setPk(TEST_ID);
        expectedAdsDto.setTitle(TEST_TITLE);
        expectedAdsDto.setPrice(newPrice);

        when(adRepository.findById(TEST_ID)).thenReturn(Optional.of(existingAd));
        when(adMapper.toAdsDto(existingAd)).thenReturn(expectedAdsDto);

        AdDto result = adService.updateDto(TEST_ID, updateProperties);
        assertNotNull(result);
        assertEquals(TEST_ID, result.getPk());
        assertEquals(TEST_TITLE, result.getTitle());
        assertEquals(newPrice, result.getPrice());
    }

    @Test
    public void testHasAdAccessUsernameTrue() {
        AdEntity ad = new AdEntity();
        UserEntity adCreator = new UserEntity();
        adCreator.setUsername(TEST_CREATOR_USERNAME);
        ad.setUser(adCreator);

        when(adRepository.findById(TEST_ID)).thenReturn(java.util.Optional.of(ad));
        when(userService.getCurrentUserRole()).thenReturn(TEST_USER_ROLE);
        when(userService.getCurrentUsername()).thenReturn(TEST_CREATOR_USERNAME);
        boolean result = adService.hasAdAccess(TEST_ID);
        assertTrue(result);
    }

    @Test
    public void testHasAdAccessUsernameFalse() {
        AdEntity ad = new AdEntity();
        UserEntity adCreator = new UserEntity();
        adCreator.setUsername(TEST_CREATOR_USERNAME);
        ad.setUser(adCreator);

        when(adRepository.findById(TEST_ID)).thenReturn(java.util.Optional.of(ad));
        when(userService.getCurrentUserRole()).thenReturn(TEST_USER_ROLE);
        when(userService.getCurrentUsername()).thenReturn(TEST_CURRENT_USERNAME);
        boolean result = adService.hasAdAccess(TEST_ID);
        assertFalse(result);
    }

    @Test
    public void testHasAdAccessAdmin() {
        AdEntity ad = new AdEntity();
        UserEntity adCreator = new UserEntity();
        adCreator.setUsername(TEST_CREATOR_USERNAME);
        ad.setUser(adCreator);

        when(adRepository.findById(TEST_ID)).thenReturn(java.util.Optional.of(ad));
        when(userService.getCurrentUserRole()).thenReturn(TEST_ADMIN_ROLE);
        when(userService.getCurrentUsername()).thenReturn(TEST_CURRENT_USERNAME);
        boolean result = adService.hasAdAccess(TEST_ID);
        assertTrue(result);
    }
}
