package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.Ad;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class CreateOrUpdateAdMapperTest {

    private final CreateOrUpdateAdMapper createOrUpdateAdMapper;

    @Autowired
    public CreateOrUpdateAdMapperTest(CreateOrUpdateAdMapper createOrUpdateAdMapper) {
        this.createOrUpdateAdMapper = createOrUpdateAdMapper;
    }

    @Test
    void shouldReturnAd() {
        Ad ad = new Ad();
        ad.setId(3L);
        ad.setPrice(100);
        ad.setTitle("test");
        ad.setDescription("test");

        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd();
        createOrUpdateAd.setPrice(100);
        createOrUpdateAd.setTitle("test");
        createOrUpdateAd.setDescription("test");

        Ad test = createOrUpdateAdMapper.toAd(createOrUpdateAd);

        assertThat(test).isNotNull();
        assertThat(test.getPrice()).isEqualTo(createOrUpdateAd.getPrice());
        assertThat(test.getTitle()).isEqualTo(createOrUpdateAd.getTitle());
        assertThat(test.getDescription()).isEqualTo(createOrUpdateAd.getDescription());

    }
}
