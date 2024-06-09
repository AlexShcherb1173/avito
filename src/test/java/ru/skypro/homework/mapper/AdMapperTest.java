package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.response.AdResponse;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AdMapperTest {

    @Autowired
    private  AdMapper adMapper;

    @Test
    public void testAdToAdResponse() {
        // Создание объекта Image
        Image avatar = Image.builder()
                .id(1L)
                .fileSize(1024)
                .mediaType("image/png")
                .data(new byte[]{1, 2, 3, 4})
                .build();

        // Создание объекта User
        User user = User.builder()
                .id(2L)
                .username("testuser")
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .phone("1234567890")
                .role(Role.USER)
                .avatar(avatar)
                .build();

        // Создание объекта Ad
        Ad ad = new Ad();
        ad.setId(3L);
        ad.setPrice(100);
        ad.setTitle("Test Title");
        ad.setDescription("Test Description");
        ad.setImage(avatar);
        ad.setUser(user);

        // Выполнение маппинга
        AdResponse adResponse = adMapper.mappingToDto(ad);

        // Проверка результатов
        assertThat(adResponse).isNotNull();
        assertThat(adResponse.getPk()).isEqualTo(3);
        assertThat(adResponse.getAuthor()).isEqualTo(2);
        assertThat(adResponse.getImage()).isEqualTo("/image/" + avatar.getId());
        assertThat(adResponse.getPrice()).isEqualTo(100);
        assertThat(adResponse.getTitle()).isEqualTo("Test Title");
    }

}