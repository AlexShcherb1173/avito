package ru.avito.dto.comment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentDtoTest {

    @Test
    void shouldUseConstructorsAndSetters() {
        CommentDto first = new CommentDto(1, "/img.jpg", "Ivan", 123L, 10, "Comment text");

        assertEquals(1, first.getAuthor());
        assertEquals("/img.jpg", first.getAuthorImage());
        assertEquals("Ivan", first.getAuthorFirstName());
        assertEquals(123L, first.getCreatedAt());
        assertEquals(10, first.getPk());
        assertEquals("Comment text", first.getText());

        CommentDto second = new CommentDto();
        second.setAuthor(1);
        second.setAuthorImage("/img.jpg");
        second.setAuthorFirstName("Ivan");
        second.setCreatedAt(123L);
        second.setPk(10);
        second.setText("Comment text");

        assertEquals(1, second.getAuthor());
        assertEquals("/img.jpg", second.getAuthorImage());
        assertEquals("Ivan", second.getAuthorFirstName());
        assertEquals(123L, second.getCreatedAt());
        assertEquals(10, second.getPk());
        assertEquals("Comment text", second.getText());
    }

    @Test
    void shouldSupportEqualsHashCodeAndToString() {
        CommentDto first = new CommentDto(1, "/img.jpg", "Ivan", 123L, 10, "Comment text");
        CommentDto second = new CommentDto(1, "/img.jpg", "Ivan", 123L, 10, "Comment text");

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertTrue(first.toString().contains("Comment text"));
    }
}