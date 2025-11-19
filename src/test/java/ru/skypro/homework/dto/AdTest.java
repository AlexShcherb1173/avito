package ru.skypro.homework.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdTest {

    @Test
    void testAdGettersAndSetters() {
        Ad ad = new Ad();
        ad.setAuthor(1);
        ad.setImage("image.jpg");
        ad.setPk(100);
        ad.setPrice(5000);
        ad.setTitle("Test Ad");

        assertEquals(1, ad.getAuthor());
        assertEquals("image.jpg", ad.getImage());
        assertEquals(100, ad.getPk());
        assertEquals(5000, ad.getPrice());
        assertEquals("Test Ad", ad.getTitle());
    }

    @Test
    void testAdEqualsAndHashCode() {
        Ad ad1 = new Ad();
        ad1.setPk(1);
        ad1.setTitle("Test");

        Ad ad2 = new Ad();
        ad2.setPk(1);
        ad2.setTitle("Test");

        assertEquals(ad1, ad2);
        assertEquals(ad1.hashCode(), ad2.hashCode());
    }
}