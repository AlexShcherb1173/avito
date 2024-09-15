package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.generatedDto.AdDto;
import ru.skypro.homework.dto.generatedDto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.generatedDto.ExtendedAdDto;
import ru.skypro.homework.dto.generatedDto.AdsDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AdMapperTest {

    private final AdMapper adMapper = Mappers.getMapper(AdMapper.class);
    @Test
    public void shouldMapAdToAdDto() {
        //given
        Image image = new Image();
        image.setId(1L);
        User testUser = new User();
        testUser.setId(42L);
        Ad ad = new Ad();
        ad.setId(1L);
        ad.setPrice(new BigDecimal(10));
        ad.setTitle("Test ads");
        ad.setAuthor(testUser);
        ad.setImage(image);



        //when
        AdDto adDto = adMapper.adToAdDto(ad);
        System.out.println(adDto);

        //then
        assertThat(adDto).isNotNull();
        assertThat(adDto.getAuthor()).isEqualTo(testUser.getId().intValue());
        assertThat(adDto.getImage()).isEqualTo("/image/" + image.getId());
        assertThat(adDto.getPk()).isEqualTo(ad.getId().intValue());
        assertThat(adDto.getPrice()).isEqualTo(ad.getPrice().intValue());
        assertThat(adDto.getTitle()).isEqualTo(ad.getTitle());
    }

    @Test
    public void shouldMapAdDtoToAd() {
        //given
        AdDto adDto = new AdDto();
        adDto.setAuthor(42);
        adDto.setImage(null);
        adDto.setPk(1);
        adDto.setPrice(10);
        adDto.setTitle("Test dto");

        //when
        Ad ads = adMapper.adDtoToAd(adDto);
        System.out.println(ads);

        //then
        assertThat(ads).isNotNull();
        assertThat(ads.getId().intValue()).isEqualTo(adDto.getPk());
        assertThat(ads.getAuthor().getId().intValue()).isEqualTo(adDto.getAuthor());
        assertThat(ads.getPrice().intValue()).isEqualTo(adDto.getPrice());
        assertThat(ads.getTitle()).isEqualTo(adDto.getTitle());
        assertThat(ads.getImage()).isNull();
        assertThat(ads.getComments()).isNull();
    }

    @Test
    public void shouldMapAdsListToAdsDto() {
        //given
        Image image = new Image();
        image.setId(1L);
        User testUser = new User();
        testUser.setId(42L);
        Ad ad1 = new Ad();
        ad1.setId(1L);
        ad1.setPrice(new BigDecimal(10));
        ad1.setTitle("Test ads");
        ad1.setAuthor(testUser);
        ad1.setImage(image);
        Ad ad2 = new Ad();
        ad2.setId(2L);
        ad2.setPrice(new BigDecimal(20));
        ad2.setTitle("Test ads 2");
        ad2.setAuthor(testUser);
        ad2.setImage(image);
        List<Ad> adList = List.of(ad1, ad2);

        //when
        AdsDto rwa = adMapper.adListToAdsDto(adList.size(), adList);
        System.out.println(rwa);

        //then
        assertThat(rwa).isNotNull();
        assertThat(rwa.getCount()).isEqualTo(adList.size());
        assertThat(rwa.getResults()).isNotNull();
        assertThat(rwa.getResults().get(0)).isEqualTo(adMapper.adToAdDto(ad1));
    }

    @Test
    public void shouldMapCreateAdDtoToAd() {
        //given
        CreateOrUpdateAdDto createOrUpdateAdDto = new CreateOrUpdateAdDto();
        createOrUpdateAdDto.setDescription("Test description");
        createOrUpdateAdDto.setPrice(10);
        createOrUpdateAdDto.setTitle("Test dto");

        //when
        Ad ads = adMapper.createOrUpdateAdDtoToAd(createOrUpdateAdDto);
        System.out.println(ads);

        //then
        assertThat(ads).isNotNull();
        assertThat(ads.getId()).isNull();
        assertThat(ads.getAuthor()).isNull();
        assertThat(ads.getPrice().intValue()).isEqualTo(createOrUpdateAdDto.getPrice());
        assertThat(ads.getTitle()).isEqualTo(createOrUpdateAdDto.getTitle());
        assertThat(ads.getDescription()).isEqualTo(createOrUpdateAdDto.getDescription());
        assertThat(ads.getImage()).isNull();
        assertThat(ads.getComments()).isNull();
    }

    @Test
    public void shouldMapAdToExtendedAdDto() {
        //given
        User testUser = new User();
        testUser.setId(42L);
        Ad ad = new Ad();
        ad.setId(1L);
        ad.setPrice(new BigDecimal(10));
        ad.setTitle("Test ads");
        ad.setAuthor(testUser);
        Image image = new Image();
        image.setId(1L);
        ad.setImage(image);

        //when
        ExtendedAdDto extendedAdDto = adMapper.adToExtendedAdDto(ad);
        System.out.println(extendedAdDto);

        //then
        assertThat(extendedAdDto).isNotNull();
        assertThat(extendedAdDto.getAuthorFirstName()).isNull();
        assertThat(extendedAdDto.getPk()).isEqualTo(ad.getId().intValue());
        assertThat(extendedAdDto.getPrice()).isEqualTo(ad.getPrice().intValue());
        assertThat(extendedAdDto.getTitle()).isEqualTo(ad.getTitle());
    }
}