package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.response.ExtendedAdResponse;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ExtendedAdResponseMapperTest {

    private final ExtendedAdResponseMapper extendedAdResponseMapper;


    @Autowired
    public ExtendedAdResponseMapperTest(ExtendedAdResponseMapper extendedAdResponseMapper) {
        this.extendedAdResponseMapper = extendedAdResponseMapper;
    }

    @Test
    void shouldReturnDTO() {

        Image avatar = Image.builder()
                .id(1L)
                .fileSize(1024)
                .mediaType("image/png")
                .data(new byte[]{1, 2, 3, 4})
                .build();

        User user = User.builder()
                .id(2L)
                .username("test")
                .password("test")
                .firstName("test")
                .lastName("test")
                .phone("3456787654")
                .role(Role.USER)
                .avatar(avatar)
                .build();

        Ad ad = new Ad();
        ad.setId(3L);
        ad.setPrice(100);
        ad.setTitle("test");
        ad.setDescription("test");
        ad.setImage(avatar);
        ad.setUser(user);

        ExtendedAdResponse mapper = extendedAdResponseMapper.toDto(ad);
        assertThat(mapper).isNotNull();
        assertThat(mapper.getPk()).isEqualTo(ad.getId());
        assertThat(mapper.getAuthorFirstName()).isEqualTo(ad.getUser().getFirstName());
        assertThat(mapper.getAuthorLastName()).isEqualTo(ad.getUser().getLastName());
        assertThat(mapper.getImage()).isEqualTo("/image/" + avatar.getId());
        assertThat(mapper.getEmail()).isEqualTo(ad.getUser().getUsername());
        assertThat(mapper.getPrice()).isEqualTo(ad.getPrice());
        assertThat(mapper.getPhone()).isEqualTo(ad.getUser().getPhone());
        assertThat(mapper.getTitle()).isEqualTo(ad.getTitle());
        assertThat(mapper.getDescription()).isEqualTo(ad.getDescription());
    }

}
