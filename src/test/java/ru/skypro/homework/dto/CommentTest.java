package ru.skypro.homework.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    @Test
    void testCommentGettersAndSetters() {
        Comment comment = new Comment();
        comment.setAuthor(1);
        comment.setAuthorImage("avatar.jpg");
        comment.setAuthorFirstName("John");
        comment.setCreatedAt(123456789L);
        comment.setPk(50);
        comment.setText("Test comment");

        assertEquals(1, comment.getAuthor());
        assertEquals("avatar.jpg", comment.getAuthorImage());
        assertEquals("John", comment.getAuthorFirstName());
        assertEquals(123456789L, comment.getCreatedAt());
        assertEquals(50, comment.getPk());
        assertEquals("Test comment", comment.getText());
    }
}