package ru.avito.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.avito.dto.ad.AdDto;
import ru.avito.dto.ad.ExtendedAdDto;
import ru.avito.entity.Ad;
import ru.avito.entity.Role;
import ru.avito.entity.User;

import static org.junit.jupiter.api.Assertions.*;

class AdMapperTest {

    private AdMapper adMapper;
    private User author;
    private Ad ad;

    @BeforeEach
    void setUp() {
        adMapper = new AdMapper();

        author = User.builder()
                .id(1)
                .email("user@example.com")
                .firstName("Ivan")
                .lastName("Ivanov")
                .phone("+79990000001")
                .role(Role.USER)
                .image("/images/users/1/avatar.jpg")
                .build();

        ad = Ad.builder()
                .id(10)
                .title("Test ad")
                .price(10000)
                .description("Test ad description")
                .image("/images/ads/10/image.jpg")
                .author(author)
                .build();
    }

    @Test
    void toDtoShouldMapAllFieldsCorrectly() {
        AdDto dto = adMapper.toDto(ad);

        assertNotNull(dto);
        assertEquals(10, dto.getPk());
        assertEquals(1, dto.getAuthor());
        assertEquals("Test ad", dto.getTitle());
        assertEquals(10000, dto.getPrice());
        assertEquals("/images/ads/10/image.jpg", dto.getImage());
    }

    @Test
    void toExtendedDtoShouldMapAllFieldsCorrectly() {
        ExtendedAdDto dto = adMapper.toExtendedDto(ad);

        assertNotNull(dto);
        assertEquals(10, dto.getPk());
        assertEquals("Test ad", dto.getTitle());
        assertEquals("Test ad description", dto.getDescription());
        assertEquals(10000, dto.getPrice());
        assertEquals("/images/ads/10/image.jpg", dto.getImage());
        assertEquals(1, dto.getAuthor());
        assertEquals("Ivan", dto.getAuthorFirstName());
        assertEquals("Ivanov", dto.getAuthorLastName());
        assertEquals("user@example.com", dto.getEmail());
        assertEquals("+79990000001", dto.getPhone());
    }
}