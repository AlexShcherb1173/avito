package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skypro.homework.dto.response.AdResponse;
import ru.skypro.homework.dto.response.AdsResponse;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class AdsMapperTest {


    private final AdsMapper adsMapper;

    @Autowired
    public AdsMapperTest(AdsMapper adsMapper) {
        this.adsMapper = adsMapper;
    }

    @Test
    void shouldReturnAdsResponse() {
        User user = User.builder()
                .id(3L)
                .username("test")
                .password("test")
                .firstName("test")
                .lastName("test")
                .build();

        Image image = Image.builder()
                .id(1L)
                .fileSize(1024)
                .mediaType("image/png")
                .data(new byte[]{1, 2, 3, 4})
                .build();

        Ad ad = Ad.builder()
                .id(1L)
                .price(100)
                .title("test")
                .description("test")
                .user(user)
                .image(image)
                .build();

        AdResponse adResponse = new AdResponse();
        adResponse.setPk(ad.getId());
        adResponse.setAuthor(ad.getUser().getId());
        adResponse.setPrice(ad.getPrice());
        adResponse.setTitle(ad.getTitle());
        adResponse.setImage("/image/" + image.getId());

        List<Ad> adList = List.of(ad);
        List<AdResponse> adResponseList = new ArrayList<>(List.of(adResponse));

        int exam = 1;

        AdsResponse adsResponse = new AdsResponse();
        adsResponse.setResults(adResponseList);
        adsResponse.setCount(adResponseList.size());

        AdsResponse adsResponse1 = adsMapper.toAdsResponse(adList);
        assertThat(adList.size()).isEqualTo(exam);
        assertThat(adsResponse1).isEqualTo(adsResponse);
    }
}
