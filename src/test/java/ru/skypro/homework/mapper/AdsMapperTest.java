package ru.skypro.homework.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;

class AdsMapperTest {

    public static final String TITLE = "Some title";
    public static final String NEW_TITLE = "Some new title";
    public static final String DESCRIPTION = "Some description";
    public static final String NEW_DESCRIPTION = "Some new description";
    public static final int PRICE = 12345;
    public static final int NEW_PRICE = 54321;
    public static final int PK = 111;
    public static final int USER_ID = 999;
    public static final String USER_FIRSTNAME = "User firstname";
    public static final String USER_LASTNAME = "User lastname";
    public static final String EMAIL = "Users e-mail";
    public static final String PHONE = "Users phone";
    public static final int IMAGE_ID = 1111;

    private final AdsMapper adsMapper;

    AdsMapperTest(AdsMapper adsMapper) {
        this.adsMapper = adsMapper;
    }

    @Test
    void toAdsDtoTest() {

        Image image = new Image();
        image.setId(IMAGE_ID);

        User user = new User();
        user.setId(USER_ID);

        Ads ads = new Ads();
        ads.setPk(PK);
        ads.setTitle(TITLE);
        ads.setDescription(DESCRIPTION);
        ads.setPrice(PRICE);
        ads.setAuthor(user);
        ads.setImage(image);

        AdsDto adsDto = adsMapper.toDto(ads);

        Assertions.assertThat(adsDto).isNotNull();
        Assertions.assertThat(adsDto.getTitle()).isEqualTo(TITLE);
        Assertions.assertThat(adsDto.getPrice()).isEqualTo(PRICE);
        Assertions.assertThat(adsDto.getPk()).isEqualTo(PK);
        Assertions.assertThat(adsDto.getAuthor()).isEqualTo(USER_ID);
        Assertions.assertThat(adsDto.getImage()).isEqualTo("/image/" + IMAGE_ID);
    }

    @Test
    void toFullAdsDtoTest() {

        Image image = new Image();
        image.setId(IMAGE_ID);

        User user = new User();
        user.setId(USER_ID);
        user.setFirstName(USER_FIRSTNAME);
        user.setLastName(USER_LASTNAME);
        user.setEmail(EMAIL);
        user.setPhone(PHONE);

        Ads ads = new Ads();
        ads.setPk(PK);
        ads.setTitle(TITLE);
        ads.setDescription(DESCRIPTION);
        ads.setPrice(PRICE);
        ads.setAuthor(user);
        ads.setImage(image);

        FullAdsDto fullAdsDto = adsMapper.toFullAdsDto(ads);

        Assertions.assertThat(fullAdsDto).isNotNull();
        Assertions.assertThat(fullAdsDto.getPk()).isEqualTo(PK);
        Assertions.assertThat(fullAdsDto.getTitle()).isEqualTo(TITLE);
        Assertions.assertThat(fullAdsDto.getDescription()).isEqualTo(DESCRIPTION);
        Assertions.assertThat(fullAdsDto.getPrice()).isEqualTo(PRICE);
        Assertions.assertThat(fullAdsDto.getAuthorFirstName()).isEqualTo(USER_FIRSTNAME);
        Assertions.assertThat(fullAdsDto.getAuthorLastName()).isEqualTo(USER_LASTNAME);
        Assertions.assertThat(fullAdsDto.getEmail()).isEqualTo(EMAIL);
        Assertions.assertThat(fullAdsDto.getPhone()).isEqualTo(PHONE);
        Assertions.assertThat(fullAdsDto.getImage()).isEqualTo("/image/" + IMAGE_ID);
    }

    @Test
    void toAdsTest() {

        CreateAdsDto createAdsDto = new CreateAdsDto();
        createAdsDto.setTitle(TITLE);
        createAdsDto.setDescription(DESCRIPTION);
        createAdsDto.setPrice(PRICE);

        Ads ads = adsMapper.toAds(createAdsDto);

        Assertions.assertThat(ads).isNotNull();
        Assertions.assertThat(ads.getTitle()).isEqualTo(TITLE);
        Assertions.assertThat(ads.getDescription()).isEqualTo(DESCRIPTION);
        Assertions.assertThat(ads.getPrice()).isEqualTo(PRICE);
        Assertions.assertThat(ads.getPk()).isNull();
        Assertions.assertThat(ads.getAuthor()).isNull();
        Assertions.assertThat(ads.getImage()).isNull();

    }

    @Test
    void updateAdsTest() {

        Image image = new Image();
        User user = new User();

        Ads ads = new Ads();
        ads.setPk(PK);
        ads.setTitle(TITLE);
        ads.setDescription(DESCRIPTION);
        ads.setPrice(PRICE);
        ads.setAuthor(user);
        ads.setImage(image);

        CreateAdsDto createAdsDto = new CreateAdsDto();
        createAdsDto.setTitle(NEW_TITLE);
        createAdsDto.setDescription(NEW_DESCRIPTION);
        createAdsDto.setPrice(NEW_PRICE);

        adsMapper.updateAds(createAdsDto, ads);

        Assertions.assertThat(ads).isNotNull();
        Assertions.assertThat(ads.getTitle()).isEqualTo(NEW_TITLE);
        Assertions.assertThat(ads.getDescription()).isEqualTo(NEW_DESCRIPTION);
        Assertions.assertThat(ads.getPrice()).isEqualTo(NEW_PRICE);
        Assertions.assertThat(ads.getPk()).isEqualTo(PK);
        Assertions.assertThat(ads.getAuthor()).isEqualTo(user);
        Assertions.assertThat(ads.getImage()).isEqualTo(image);
    }
}