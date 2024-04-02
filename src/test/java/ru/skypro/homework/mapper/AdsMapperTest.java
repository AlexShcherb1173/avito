package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@Transactional
class AdsMapperTest {
    private final AdsMapper adsMapper = Mappers.getMapper(AdsMapper.class);

    @Test
    void adsToAdsDto_shouldMapAdsToAdDto() {
        //given
        User testUser = new User();
        testUser.setId(50);
        Ads ads = new Ads();
        ads.setId(60);
        ads.setPrice(500);
        ads.setTitle("Test ads");
        ads.setUser(testUser);

        //when
        AdDto adDto = adsMapper.adsToAdsDto(ads);
        System.out.println(adDto);

        //then
        assertThat(adDto).isNotNull();
        assertThat(adDto.getAuthor()).isEqualTo(testUser.getId().intValue());
        assertThat(adDto.getPk()).isEqualTo(ads.getId().intValue());
        assertThat(adDto.getPrice()).isEqualTo(ads.getPrice().intValue());
        assertThat(adDto.getTitle()).isEqualTo(ads.getTitle());
    }

    @Test
    void listAdsToAdsDto_shouldMapAdsListToAdsDto() {
        //given
        User testUser = new User();
        testUser.setId(55);
        Ads ads1 = new Ads();
        ads1.setId(1);
        ads1.setPrice(200);
        ads1.setTitle("Test ads");
        ads1.setUser(testUser);
        Ads ads2 = new Ads();
        ads2.setId(2);
        ads2.setPrice(300);
        ads2.setTitle("Test ads 2");
        ads2.setUser(testUser);
        List<Ads> adsList = List.of(ads1, ads2);

        //when
        AdsDto adsDto = adsMapper.listAdsToAdsDto(adsList.size(), adsList);
        System.out.println(adsDto);

        //then
        assertThat(adsDto).isNotNull();
        assertThat(adsDto.getCount()).isEqualTo(adsList.size());
        assertThat(adsDto.getResults()).isNotNull();
        assertThat(adsDto.getResults().get(0)).isEqualTo(adsMapper.adsToAdsDto(ads1));
    }

    @Test
    void createOrUpdateAdDtoToAds() {
        //given
        CreateOrUpdateAdDto createOrUpdateAdDto = new CreateOrUpdateAdDto();
        createOrUpdateAdDto.setDescription("Test description");
        createOrUpdateAdDto.setPrice(10);
        createOrUpdateAdDto.setTitle("Test dto");

        //when
        Ads ads = adsMapper.createOrUpdateAdDtoToAds(createOrUpdateAdDto);
        System.out.println(ads);

        //then
        assertThat(ads).isNotNull();
        assertThat(ads.getId()).isNull();
        assertThat(ads.getUser()).isNull();
        assertThat(ads.getPrice().intValue()).isEqualTo(createOrUpdateAdDto.getPrice());
        assertThat(ads.getTitle()).isEqualTo(createOrUpdateAdDto.getTitle());
        assertThat(ads.getDescription()).isEqualTo(createOrUpdateAdDto.getDescription());
        assertThat(ads.getImage()).isNull();

    }

    @Test
    void toExtendedAdDto() {
        //given
        User testUser = new User();
        testUser.setId(60);
        Ads ads = new Ads();
        ads.setId(10);
        ads.setPrice(5000);
        ads.setTitle("Test ads");
        ads.setUser(testUser);

        //when
        ExtendedAdDto extendedAdDto = adsMapper.toExtendedAdDto(ads);
        System.out.println(extendedAdDto);

        //then
        assertThat(extendedAdDto).isNotNull();
        assertThat(extendedAdDto.getAuthorFirstName()).isNull();
        assertThat(extendedAdDto.getPk()).isEqualTo(ads.getId().intValue());
        assertThat(extendedAdDto.getPrice()).isEqualTo(ads.getPrice().intValue());
        assertThat(extendedAdDto.getTitle()).isEqualTo(ads.getTitle());
    }
}