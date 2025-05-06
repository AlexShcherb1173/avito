package ru.skypro.homework.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.ExtendedAdDTO;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdMapperTest {

    private AdMapper adMapper;

    @BeforeEach
    public void setUp() {
        adMapper = Mappers.getMapper(AdMapper.class);
    }

    @Test
    public void testToModel_AdDTO() {
        // Arrange
        AdDTO adDTO = new AdDTO();
        adDTO.setAuthor(1);
        adDTO.setImage(String.valueOf(2));
        adDTO.setPk(3);
        adDTO.setTitle("Test Ad");
        adDTO.setPrice((int) 100.0);

        // Act
        Ad ad = adMapper.toModel(adDTO);

        // Assert
        assertEquals(1, ad.getAuthor().getId());
        assertEquals(2, ad.getImage().getId());
        assertEquals(3, ad.getId());
        assertEquals("Test Ad", ad.getTitle());
        assertEquals(100.0, ad.getPrice());
    }

    @Test
    public void testToModel_CreateOrUpdateAdDTO() {
        // Arrange
        CreateOrUpdateAdDTO createOrUpdateAdDTO = new CreateOrUpdateAdDTO();
        createOrUpdateAdDTO.setTitle("New Ad");
        createOrUpdateAdDTO.setPrice((int) 150.0);

        // Act
        Ad ad = adMapper.toModel(createOrUpdateAdDTO);

        // Assert
        assertEquals("New Ad", ad.getTitle());
        assertEquals(150.0, ad.getPrice());
    }

    @Test
    public void testToModel_ExtendedAdDTO() {
        // Arrange
        ExtendedAdDTO extendedAdDTO = new ExtendedAdDTO();
        extendedAdDTO.setPk(4);
        extendedAdDTO.setAuthorFirstName("John");
        extendedAdDTO.setAuthorLastName("Doe");
        extendedAdDTO.setEmail("john.doe@example.com");
        extendedAdDTO.setImage(String.valueOf(5));
        extendedAdDTO.setPhone("123456789");
        extendedAdDTO.setTitle("Extended Ad");
        extendedAdDTO.setPrice((int) 200.0);

        // Act
        Ad ad = adMapper.toModel(extendedAdDTO);

        // Assert
        assertEquals(4, ad.getId());
        assertEquals("John", ad.getAuthor().getFirstName());
        assertEquals("Doe", ad.getAuthor().getLastName());
        assertEquals("john.doe@example.com", ad.getAuthor().getEmail());
        assertEquals(5, ad.getImage().getId());
        assertEquals("123456789", ad.getAuthor().getPhone());
        assertEquals("Extended Ad", ad.getTitle());
        assertEquals(200.0, ad.getPrice());
    }

    @Test
    public void testToDtoAdDTO() {
        // Arrange
        Ad ad = new Ad();
        ad.setId(6);

        Image image = new Image();
        image.setId(7);

        ad.setImage(image);

        User author = new User();
        author.setId(8);
        author.setFirstName("Alice");
        author.setLastName("Smith");
        author.setEmail("alice.smith@example.com");

        ad.setAuthor(author);
        ad.setTitle("Ad for Testing");
        ad.setPrice((int) 300.0);

        // Act
        AdDTO adDTO = adMapper.toDtoAdDTO(ad);

        // Assert
        assertEquals(8, adDTO.getAuthor());
        assertEquals("7", adDTO.getImage());
        assertEquals(6, adDTO.getPk());
        assertEquals("Ad for Testing", adDTO.getTitle());
        assertEquals(300, adDTO.getPrice());
    }

    @Test
    public void testToDtoExtendedAdDTO() {
        // Arrange
        Ad ad = new Ad();

        Image image = new Image();
        image.setId(9);

        ad.setImage(image);

        User author = new User();
        author.setFirstName("Bob");
        author.setLastName("Johnson");
        author.setEmail("bob.johnson@example.com");

        ad.setAuthor(author);

        ad.setId(10);
        ad.setTitle("Extended Test Ad");
        // Act
        ExtendedAdDTO extendedAdDTO = adMapper.toDtoExtendedAdDTO(ad);
        // Assert
        assertEquals(10, extendedAdDTO.getPk());
        assertEquals("Bob", extendedAdDTO.getAuthorFirstName());
        assertEquals("Johnson", extendedAdDTO.getAuthorLastName());
        assertEquals("bob.johnson@example.com", extendedAdDTO.getEmail());
        assertEquals("9", extendedAdDTO.getImage());
    }
}