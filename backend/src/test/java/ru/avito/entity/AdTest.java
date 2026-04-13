package ru.avito.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdTest {

    @Test
    void shouldCreateAdWithBuilder() {
        User author = User.builder()
                .id(1)
                .email("test@mail.com")
                .password("pass")
                .firstName("John")
                .lastName("Doe")
                .phone("123")
                .role(Role.USER)
                .build();

        Ad ad = Ad.builder()
                .id(1)
                .title("Test Ad")
                .price(100)
                .description("Description")
                .image("img.png")
                .author(author)
                .build();

        assertEquals(1, ad.getId());
        assertEquals("Test Ad", ad.getTitle());
        assertEquals(100, ad.getPrice());
        assertEquals("Description", ad.getDescription());
        assertEquals("img.png", ad.getImage());
        assertEquals(author, ad.getAuthor());
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        Ad ad1 = Ad.builder().id(1).title("A").price(1).description("D").build();
        Ad ad2 = Ad.builder().id(1).title("A").price(1).description("D").build();

        assertEquals(ad1, ad2);
        assertEquals(ad1.hashCode(), ad2.hashCode());
    }

    @Test
    void shouldTestSetters() {
        Ad ad = new Ad();
        ad.setId(1);
        ad.setTitle("Title");
        ad.setPrice(200);
        ad.setDescription("Desc");

        assertEquals(1, ad.getId());
        assertEquals("Title", ad.getTitle());
        assertEquals(200, ad.getPrice());
        assertEquals("Desc", ad.getDescription());
    }

    @Test
    void shouldBuildAdCorrectly() {
        User author = User.builder().id(1).build();

        Ad ad = Ad.builder()
                .id(10)
                .title("Ad")
                .description("Desc")
                .price(1000)
                .image("/img.jpg")
                .author(author)
                .build();

        assertEquals(10, ad.getId());
        assertEquals("Ad", ad.getTitle());
        assertEquals("Desc", ad.getDescription());
        assertEquals(1000, ad.getPrice());
        assertEquals("/img.jpg", ad.getImage());
        assertEquals(author, ad.getAuthor());
    }
}